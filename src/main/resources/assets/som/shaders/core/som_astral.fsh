#version 150

uniform vec4 ColorModulator;
uniform float SomKind;
uniform float SomTime;
uniform float SomSpin;

in vec4 tint;
in vec2 coord;
in vec3 world;

out vec4 fragColor;

// everything gets snapped onto a grid before it is shaded, so what comes out reads as a texture
// somebody drew rather than a smooth gradient
vec2 pixels(vec2 uv, float grid) {
    return (floor(uv * grid) + 0.5) / grid;
}

float hash(vec3 p) {
    p = fract(p * 0.3183099 + vec3(0.71, 0.113, 0.419));
    p *= 17.0;
    return fract(p.x * p.y * p.z * (p.x + p.y + p.z));
}

float noise(vec3 p) {
    vec3 i = floor(p);
    vec3 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);
    return mix(mix(mix(hash(i + vec3(0,0,0)), hash(i + vec3(1,0,0)), f.x),
                   mix(hash(i + vec3(0,1,0)), hash(i + vec3(1,1,0)), f.x), f.y),
               mix(mix(hash(i + vec3(0,0,1)), hash(i + vec3(1,0,1)), f.x),
                   mix(hash(i + vec3(0,1,1)), hash(i + vec3(1,1,1)), f.x), f.y), f.z);
}

float fbm(vec3 p) {
    float sum = 0.0;
    float amp = 0.5;
    for (int i = 0; i < 4; i++) {
        sum += noise(p) * amp;
        p *= 2.03;
        amp *= 0.5;
    }
    return sum;
}

// steps the colour down onto a short ladder the way a hand drawn texture would sit, then pushes
// the colour back up so the ladder does not wash it out
vec3 postery(vec3 c, float steps, float punch) {
    float luma = dot(c, vec3(0.2126, 0.7152, 0.0722));
    c = mix(vec3(luma), c, punch);
    return floor(c * steps + 0.5) / steps;
}

void main() {
    int kind = int(SomKind + 0.5);
    vec4 out_colour;

    if (kind == 0) {
        // a star. a solid pixel cross rather than a soft dot, like the vanilla ones
        vec2 g = pixels(coord, 7.0);
        vec2 p = g * 2.0 - 1.0;
        float d = max(abs(p.x), abs(p.y));
        float cross = min(abs(p.x), abs(p.y));
        if (d > 1.0 || cross > 0.45) discard;
        float body = d < 0.32 ? 1.0 : (d < 0.62 ? 0.55 : 0.22);
        float blink = 0.85 + 0.15 * sin(SomTime * 1.7 + floor(world.x) * 0.9 + floor(world.z) * 0.6);
        out_colour = vec4(postery(tint.rgb, 8.0, 1.35), tint.a * body * blink);

    } else if (kind == 1) {
        // nebula. big soft cloud stepped down into blocky bands, faded right out at its own edge
        vec2 g = pixels(coord, 26.0);
        vec2 p = g * 2.0 - 1.0;
        float r = length(p);
        if (r > 1.0) discard;
        vec3 drift = vec3(floor(world * 0.35) * 0.02) + vec3(SomTime * 0.010, SomTime * 0.006, 0.0);
        float cloud = fbm(drift * 3.0 + vec3(g * 3.1, 0.0));
        cloud = pow(max(0.0, cloud - 0.30) * 1.7, 1.5);
        cloud = floor(cloud * 5.0 + 0.5) / 5.0;
        float edge = pow(max(0.0, 1.0 - r), 2.4);
        out_colour = vec4(postery(tint.rgb, 7.0, 1.5), tint.a * cloud * edge);

    } else if (kind == 2) {
        // a constellation link. one pixel wide with a dotted feel to it
        vec2 g = pixels(coord, vec2(48.0, 3.0).x);
        float across = abs(g.y * 2.0 - 1.0);
        float dash = step(0.35, fract(g.x * 24.0));
        if (across > 0.55) discard;
        out_colour = vec4(postery(tint.rgb, 6.0, 1.2), tint.a * dash * 0.55);

    } else if (kind == 3) {
        // a planet. shaded off a real sphere normal, then stepped so the light falls in bands
        vec2 g = pixels(coord, 26.0);
        vec2 p = g * 2.0 - 1.0;
        float r2 = dot(p, p);
        if (r2 > 1.0) discard;
        vec3 n = vec3(p, sqrt(max(0.0, 1.0 - r2)));
        float c = cos(SomSpin);
        float s = sin(SomSpin);
        vec3 spun = vec3(n.x * c - n.z * s, n.y, n.x * s + n.z * c);

        float bands = fbm(spun * 3.2);
        float land = step(0.52, fbm(spun * 5.5 + 11.0));
        vec3 surface = mix(tint.rgb * (0.6 + bands * 0.8), tint.rgb * 1.45, land * 0.6);

        vec3 sun = normalize(vec3(-0.55, 0.42, 0.72));
        float lit = max(0.0, dot(n, sun));
        lit = floor(lit * 5.0 + 0.5) / 5.0;
        float rim = pow(1.0 - max(0.0, n.z), 4.0);
        vec3 body = surface * (0.14 + lit * 1.25) + tint.rgb * rim * 0.45;
        out_colour = vec4(postery(body, 10.0, 1.4), tint.a);

    } else if (kind == 4) {
        // the air around a planet, a couple of stepped rings hugging the limb
        vec2 g = pixels(coord, 30.0);
        float d = length(g * 2.0 - 1.0);
        if (d > 1.0) discard;
        float shell = smoothstep(0.60, 0.82, d) * (1.0 - smoothstep(0.82, 1.0, d));
        shell = floor(shell * 4.0 + 0.5) / 4.0;
        out_colour = vec4(postery(tint.rgb, 6.0, 1.6), tint.a * shell);

    } else {
        // rings, an ellipse squashed onto the planets plane and cut into stepped bands
        vec2 g = pixels(coord, 34.0);
        vec2 p = g * 2.0 - 1.0;
        vec2 e = vec2(p.x, p.y * 3.6);
        float d = length(e);
        if (d > 1.0 || d < 0.44) discard;
        float band = step(0.45, fract(d * 9.0));
        float fade = smoothstep(0.44, 0.52, d) * (1.0 - smoothstep(0.84, 1.0, d));
        out_colour = vec4(postery(tint.rgb, 6.0, 1.4), tint.a * band * fade * 0.8);
    }

    fragColor = out_colour * ColorModulator;
}
