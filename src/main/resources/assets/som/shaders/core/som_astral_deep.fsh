#version 150

uniform sampler2D Sampler0;

uniform float GameTime;
uniform int SomLayers;
uniform vec3 SomShift;
uniform vec4 SomNear;
uniform vec4 SomFar;
uniform float SomGain;

in vec3 look;

out vec4 fragColor;

mat2 spin(float radians) {
    float c = cos(radians);
    float s = sin(radians);
    return mat2(c, -s, s, c);
}

// three flat samples blended by which way the direction points. no wrap anywhere in it, so there
// is no line down the sky where a wrap used to be
float grain(vec3 d, float scale, mat2 turn, vec2 slide) {
    vec3 w = abs(d);
    w = w / (w.x + w.y + w.z);
    w *= w;
    w /= (w.x + w.y + w.z);

    float x = texture(Sampler0, turn * (d.zy * scale) + slide).r;
    float y = texture(Sampler0, turn * (d.xz * scale) + slide).r;
    float z = texture(Sampler0, turn * (d.xy * scale) + slide).r;
    return x * w.x + y * w.y + z * w.z;
}

// the filmic curve a shader pack puts on the end of its chain. it keeps the colour in the bright
// places instead of clipping them all off to white
vec3 filmic(vec3 c) {
    vec3 a = c * (2.51 * c + 0.03);
    vec3 b = c * (2.43 * c + 0.59) + 0.14;
    return clamp(a / b, 0.0, 1.0);
}

void main() {
    vec3 d = normalize(look);

    // the big soft colour the whole sky sits on. without it everything else is specks on black
    mat2 slow = spin(0.7);
    float wash = grain(d, 0.75, slow, vec2(GameTime * 0.4, GameTime * 0.25));
    float wash2 = grain(d, 1.6, spin(2.1), vec2(-GameTime * 0.3, GameTime * 0.5));
    float cloud = pow(clamp(wash * 0.7 + wash2 * 0.5, 0.0, 1.0), 1.6);

    vec3 sum = mix(SomFar.rgb, SomNear.rgb, cloud) * cloud * 1.35;

    float bright = 0.0;
    for (int i = 0; i < SomLayers; i++) {
        float layer = float(i + 1);
        float pull = layer / float(SomLayers);

        // same shape the end portal uses, but tiled far tighter so it reads as a field of stars
        // rather than a few stretched smudges
        float scale = (4.5 - layer / 4.0) * 3.4;
        mat2 turn = spin(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));

        // walking drags the near layers across the far ones, which is the depth
        vec2 walk = vec2(SomShift.x + SomShift.z * 0.35, SomShift.y) * pull * pull;
        vec2 slide = vec2(17.0 / layer, (2.0 + layer / 1.5) * GameTime * 1.5) + walk;

        float g = grain(d, scale, turn, slide);
        g = pow(max(0.0, g - 0.04) * 1.25, 1.25);

        vec3 tone = mix(SomFar.rgb, SomNear.rgb, pull);
        float weight = 0.16 + pull * 0.48;
        sum += g * tone * weight;
        bright += g * weight;
    }

    // a cheap stand in for bloom, the brightest places bleed their own colour back over themselves
    sum += sum * bright * 1.1;

    sum = filmic(sum * SomGain * 2.1);
    sum = floor(sum * 44.0 + 0.5) / 44.0;

    fragColor = vec4(sum, 1.0);
}
