package com.volunteernews24.app.ui.screens

val SPLASH_HTML = """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Volunteer News 24</title>
<style>
html,body{
    margin:0;
    padding:0;
    width:100%;
    height:100%;
    background:#000;
    overflow:hidden;
}
body{
    display:flex;
    justify-content:center;
    align-items:center;
}
svg{
    width:100%;
    height:100%;
    max-width:480px;
    background:#000;
}
/* =====================================
   VOLUNTEER
===================================== */
#volunteer{
    opacity:0;
    transform-origin:center;
    animation: volunteerIn 1s cubic-bezier(.17,.67,.3,1.4) forwards;
}
@keyframes volunteerIn{
    0%{ opacity:0; transform: translateX(-180px) scale(.7); filter:blur(8px); }
    70%{ opacity:1; transform: translateX(8px) scale(1.04); filter:blur(0); }
    100%{ opacity:1; transform: translateX(0) scale(1); }
}
/* =====================================
   NEWS
===================================== */
#news{
    opacity:0;
    animation: newsIn 1s ease-out .35s forwards;
}
@keyframes newsIn{
    0%{ opacity:0; transform: translateX(-160px) scale(.7); filter:blur(8px); }
    70%{ opacity:1; transform: translateX(8px) scale(1.04); filter:blur(0); }
    100%{ opacity:1; transform: translateX(0) scale(1); }
}
/* =====================================
   GLOBE
===================================== */
#globeGroup{
    opacity:0;
    transform-origin: 350px 365px;
    animation: globeIn 1.1s cubic-bezier(.2,.8,.2,1) .5s forwards;
}
@keyframes globeIn{
    0%{ opacity:0; transform: scale(.3) rotate(-60deg); }
    80%{ opacity:1; transform: scale(1.08) rotate(5deg); }
    100%{ opacity:1; transform: scale(1) rotate(0deg); }
}
/* =====================================
   GLOBE FLOAT
===================================== */
#globeSpin{
    transform-origin: 350px 365px;
    animation: globeFloat 4s ease-in-out infinite alternate;
}
@keyframes globeFloat{
    from{ transform: translateY(-3px); }
    to{ transform: translateY(4px); }
}
/* =====================================
   24 — BIG & CENTERED
===================================== */
#number24{
    opacity:0;
    transform-origin: 350px 365px;
    animation: numberSlide 1.15s cubic-bezier(.15,.8,.25,1.25) 1s forwards;
}
@keyframes numberSlide{
    0%{ opacity:0; transform: translateX(150px) translateY(-60px) scale(.35) rotate(20deg); filter:blur(8px); }
    70%{ opacity:1; transform: translateX(-4px) translateY(2px) scale(1.05) rotate(-2deg); filter:blur(0); }
    100%{ opacity:1; transform: translateX(0) translateY(0) scale(1) rotate(0deg); }
}
/* =====================================
   WELCOME
===================================== */
#welcome{
    opacity:0;
    animation: welcomeIn 1s ease-out 1.7s forwards, welcomeGlow 1.5s ease-in-out 2.8s infinite alternate;
}
@keyframes welcomeIn{
    from{ opacity:0; transform: translateY(80px) scale(.8); filter:blur(8px); }
    to{ opacity:1; transform: translateY(0) scale(1); filter:blur(0); }
}
@keyframes welcomeGlow{
    from{ filter: drop-shadow(0 0 4px #ffaa00); }
    to{ filter: drop-shadow(0 0 18px #ffcc00); }
}
/* =====================================
   ORBIT
===================================== */
#orbit1{
    stroke-dasharray:500;
    stroke-dashoffset:500;
    animation: drawOrbit 1.3s ease-out .7s forwards, orbitPulse 2s ease-in-out 2s infinite alternate;
}
#orbit2{
    stroke-dasharray:500;
    stroke-dashoffset:500;
    animation: drawOrbit 1.3s ease-out .9s forwards, orbitPulse 2s ease-in-out 2.2s infinite alternate;
}
@keyframes drawOrbit{
    to{ stroke-dashoffset:0; }
}
@keyframes orbitPulse{
    from{ opacity:.4; }
    to{ opacity:1; }
}
</style>
</head>
<body>
<svg viewBox="0 0 480 853" xmlns="http://www.w3.org/2000/svg">
<defs>
<radialGradient id="globeGradient" cx="35%" cy="25%">
    <stop offset="0%" stop-color="#63d8ff"/>
    <stop offset="40%" stop-color="#087cff"/>
    <stop offset="75%" stop-color="#003d9e"/>
    <stop offset="100%" stop-color="#001329"/>
</radialGradient>
<linearGradient id="red3d" x1="0" y1="0" x2="0" y2="1">
    <stop offset="0%" stop-color="#ff6969"/>
    <stop offset="35%" stop-color="#ff1515"/>
    <stop offset="100%" stop-color="#8d0000"/>
</linearGradient>
<linearGradient id="gold3d" x1="0" y1="0" x2="0" y2="1">
    <stop offset="0%" stop-color="#fff89c"/>
    <stop offset="35%" stop-color="#ffd500"/>
    <stop offset="75%" stop-color="#f0a800"/>
    <stop offset="100%" stop-color="#a96000"/>
</linearGradient>
<linearGradient id="silver3d" x1="0" y1="0" x2="0" y2="1">
    <stop offset="0%" stop-color="#ffffff"/>
    <stop offset="40%" stop-color="#f5f5f5"/>
    <stop offset="70%" stop-color="#bfc6d0"/>
    <stop offset="100%" stop-color="#737b87"/>
</linearGradient>
<filter id="blueGlow">
    <feGaussianBlur stdDeviation="4" result="blur"/>
    <feMerge><feMergeNode in="blur"/><feMergeNode in="SourceGraphic"/></feMerge>
</filter>
<filter id="redGlow">
    <feGaussianBlur stdDeviation="4" result="blur"/>
    <feMerge><feMergeNode in="blur"/><feMergeNode in="SourceGraphic"/></feMerge>
</filter>
<filter id="shadow">
    <feDropShadow dx="0" dy="6" stdDeviation="4" flood-color="#000" flood-opacity=".8"/>
</filter>
<clipPath id="globeClip">
    <circle cx="350" cy="365" r="76"/>
</clipPath>
</defs>
<rect width="480" height="853" fill="#000"/>
<circle cx="350" cy="365" r="105" fill="#0066ff" opacity=".12" filter="url(#blueGlow)"/>
<ellipse id="orbit1" cx="300" cy="385" rx="210" ry="58" fill="none" stroke="#ff2020" stroke-width="4" transform="rotate(-8 300 385)" filter="url(#redGlow)"/>
<ellipse id="orbit2" cx="300" cy="385" rx="205" ry="52" fill="none" stroke="#188aff" stroke-width="3" transform="rotate(7 300 385)" filter="url(#blueGlow)"/>
<g id="volunteer" filter="url(#shadow)">
    <text x="28" y="270" font-family="Arial Black,Arial,sans-serif" font-size="58" font-weight="900" fill="#720000">Volunteer</text>
    <text x="23" y="263" font-family="Arial Black,Arial,sans-serif" font-size="58" font-weight="900" fill="url(#red3d)" stroke="#ff7777" stroke-width="1">Volunteer</text>
</g>
<g id="news" filter="url(#shadow)">
    <text x="25" y="335" font-family="Arial Black,Arial,sans-serif" font-size="68" font-weight="900" fill="#555">News</text>
    <text x="19" y="328" font-family="Arial Black,Arial,sans-serif" font-size="68" font-weight="900" fill="url(#silver3d)">News</text>
</g>
<g id="globeGroup">
<g id="globeSpin">
    <circle cx="350" cy="365" r="76" fill="url(#globeGradient)" stroke="#55d5ff" stroke-width="3" filter="url(#blueGlow)"/>
    <ellipse cx="350" cy="365" rx="38" ry="76" fill="none" stroke="#8cddff" stroke-width="1" opacity=".45"/>
    <ellipse cx="350" cy="365" rx="60" ry="76" fill="none" stroke="#8cddff" stroke-width="1" opacity=".22"/>
    <ellipse cx="350" cy="365" rx="74" ry="24" fill="none" stroke="#8cddff" stroke-width="1" opacity=".4"/>
    <ellipse cx="350" cy="365" rx="74" ry="48" fill="none" stroke="#8cddff" stroke-width="1" opacity=".22"/>
    <g clip-path="url(#globeClip)" fill="#c4f0ff" opacity=".9">
        <path d="M305 320 C318 311 335 312 341 319 C346 327 338 335 328 337 C319 341 317 349 307 350 C299 341 298 328 305 320Z"/>
        <path d="M353 320 C367 315 387 319 395 330 C386 334 377 333 373 342 C364 348 363 359 354 365 C347 354 346 340 352 333 C356 329 349 325 353 320Z"/>
        <path d="M330 368 C341 363 351 367 356 376 C356 386 349 395 347 407 C340 413 331 405 329 394 C325 384 321 375 330 368Z"/>
    </g>
    <circle cx="325" cy="337" r="13" fill="#fff" opacity=".14"/>
</g>
</g>
<g id="number24" filter="url(#shadow)">
    <text x="309" y="391" text-anchor="middle" font-family="Arial Black,Arial,sans-serif" font-size="78" font-weight="900" fill="#650000">24</text>
    <text x="305" y="385" text-anchor="middle" font-family="Arial Black,Arial,sans-serif" font-size="78" font-weight="900" fill="url(#red3d)" stroke="#ff8a8a" stroke-width="1.5">24</text>
</g>
<g id="welcome" filter="url(#shadow)">
    <text x="95" y="555" font-family="Arial Black,Arial,sans-serif" font-size="58" font-weight="900" fill="#996200">Welcome</text>
    <text x="90" y="548" font-family="Arial Black,Arial,sans-serif" font-size="58" font-weight="900" fill="url(#gold3d)" stroke="#fff07c" stroke-width="1">Welcome</text>
</g>
<ellipse cx="350" cy="450" rx="100" ry="10" fill="#008cff" opacity=".15" filter="url(#blueGlow)"/>
</svg>
</body>
</html>
"""
