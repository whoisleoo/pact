@echo off
:: Configura o terminal para UTF-8 para exibir os caracteres especiais e cores ANSI corretamente
chcp 65001 >nul

echo ==========================================================
echo            INICIALIZADOR DO PROJETO PACT
echo ==========================================================
echo.

:: 1. Verificar se o MySQL do XAMPP está ativo
echo [1/3] Verificando conexao com o banco de dados MySQL...
"c:\xampp\mysql\bin\mysqladmin.exe" -u root ping >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERRO] O MySQL nao esta ativo na porta 3306!
    echo.
    echo Certifique-se de que o XAMPP Control Panel esta aberto
    echo e o modulo MySQL esta iniciado (verde).
    echo.
    pause
    exit /b
)
echo [OK] Banco de dados ativo e pronto para conexao!
echo.

:: 2. Entrar na pasta do Backend e Compilar
cd /d "%~dp0\Backend"
echo [2/3] Compilando o projeto com Maven...
call mvn compile
if %errorlevel% neq 0 (
    echo [ERRO] Ocorreu um erro durante a compilacao do Maven.
    echo.
    pause
    exit /b
)
echo.

:: 3. Executar o PACT
echo [3/3] Iniciando a aplicacao CLI...
echo ==========================================================
echo.
call mvn exec:java
echo.
echo ==========================================================
echo Aplicacao encerrada.
pause
