#version 150

#moj_import <lodestone:common_math.glsl>

uniform sampler2D Sampler0;
uniform float LumiTransparency;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec2 uv = texCoord0;
    float y = uv.y;
    float width = y;
    if (width > 0.7) {
        float delta = pow((width - 0.7) / 0.3, 2.0);
        width = uv.y * (1.0-delta);
    }
    if (abs(uv.x-0.5)*2. > width) {
        return;
    }
    uv.x -= 0.5*(1.-width);
    if (width != 0.){
        uv.x /= width;
    }
    vec4 color = transformColor(texture(Sampler0, texCoord0), LumiTransparency, vertexColor, ColorModulator);
    fragColor = applyFog(color, FogStart, FogEnd, FogColor, vertexDistance);
}
