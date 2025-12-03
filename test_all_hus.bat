@echo off
cls
echo ===========================================================================
echo EJECUTANDO PRUEBAS DE LAS 6 HUs
echo ===========================================================================
echo.

echo NOTA: Este script asume que la aplicación NO está corriendo
echo       porque los tests usarán su propio contexto de Spring.
echo.

echo [1] Deteniendo aplicación si está corriendo...
taskkill /F /IM java.exe 2>nul
timeout /t 2 /nobreak >nul

echo.
echo [2] Ejecutando todas las pruebas...
echo.
mvn clean test

echo.
echo [3] Verificando cobertura de tests...
echo.
if exist "target\surefire-reports" (
    echo Reportes de test generados en: target\surefire-reports
    dir "target\surefire-reports\*.txt"
) else (
    echo No se generaron reportes de test
)

echo.
echo [4] Resumen...
echo.
if %errorlevel% equ 0 (
    echo ✅ TODAS LAS PRUEBAS PASARON
) else (
    echo ❌ ALGUNAS PRUEBAS FALLARON
    echo Revisa los logs arriba para ver detalles
)

echo.
pause