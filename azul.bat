@Echo Off
cls
del %1.sal 2> nul
del %1.asm 2> nul
java azulAL %1
if errorlevel 1 goto Fallo
echo ANALISIS LEXICOGRAFICO CORRECTO
java azulSLR1 %1
:Fallo
ECHO Compilacion terminada
rem del %1.sal 2> nul