#version 150

uniform sampler2D Sampler0;

uniform mat4 InverseVP;
uniform vec4 LightPos;
uniform vec4 LightColor;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    float depth = texture(Sampler0, texCoord).r;
    if (depth >= 1.0) {
        discard;
    }

    vec4 ndc = vec4(texCoord * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    vec4 unprojected = InverseVP * ndc;
    if (abs(unprojected.w) < 1.0e-6) {
        discard;
    }
    vec3 surface = unprojected.xyz / unprojected.w;

    vec3 toLight = LightPos.xyz - surface;
    float dist = length(toLight);
    if (dist > LightPos.w || dist < 1.0e-4) {
        discard;
    }

    vec3 ddx = dFdx(surface);
    vec3 ddy = dFdy(surface);
    vec3 raw = cross(ddx, ddy);
    float len = length(raw);

    // How far apart the neighbouring pixels sit. On one flat face this is about the
    // width of a pixel; across a silhouette, a block corner or a pane a pixel thin it
    // leaps, and the normal built from it is nonsense.
    float step = max(length(ddx), length(ddy));
    float expected = 0.015 + dist * 0.015;

    // Faded rather than switched. Choosing between the two outright is what drew the
    // hard rectangles: neighbouring pixels landed on opposite sides of the test and
    // jumped between two different brightnesses.
    float suspect = smoothstep(expected * 0.4, expected * 8.0, step);
    if (len < 1.0e-9) {
        suspect = 1.0;
    }

    vec3 normal = len < 1.0e-9 ? vec3(0.0, 1.0, 0.0) : raw / len;
    if (dot(normal, -normalize(surface)) < 0.0) {
        normal = -normal;
    }

    // Wrapped rather than clamped at the terminator, so a slightly wrong normal only
    // shifts the shading a little instead of flipping it between lit and black.
    float lambert = dot(normal, toLight / dist) * 0.5 + 0.5;
    lambert = mix(0.45, 1.0, lambert * lambert);
    float facing = mix(lambert, 0.6, suspect);

    float fall = 1.0 - dist / LightPos.w;
    fall = fall * fall * fall;

    // Eases off right at the flame as well, so the block holding it is lit rather
    // than drowned in flat colour.
    fall *= smoothstep(0.0, 0.9, dist);

    vec3 lit = LightColor.rgb * fall * facing * LightColor.a;

    // a touch of noise, otherwise the falloff steps in visible rings on flat ground
    float grain = fract(sin(dot(gl_FragCoord.xy, vec2(12.9898, 78.233))) * 43758.5453);
    lit += (grain - 0.5) * (1.5 / 255.0);

    fragColor = vec4(clamp(lit, 0.0, 1.0), 1.0);
}
