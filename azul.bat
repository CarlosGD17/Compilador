@Echo Off
cls
del %1.sal 2> nul
java azulAL %1
if errorlevel 1 goto Fallo
java azulSLR1 %1
:Fallo