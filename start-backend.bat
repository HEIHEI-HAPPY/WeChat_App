@echo off
chcp 65001 >nul
cd /d D:\微信\项目库\mall\server
echo ========================================
echo   Coffee  - Spring Boot Backend
echo   端口: 3000
echo ========================================
echo.

REM 杀掉残留的 java 进程
taskkill /F /IM java.exe 2>nul >nul
timeout /t 2 /nobreak >nul

REM 启动后端
java -jar target\mall-server-0.1.0.jar

REM 如果 jar 不存在，先编译
if %ERRORLEVEL% neq 0 (
    echo.
    echo [错误] 找不到 JAR，正在尝试重新编译...
    mvn -DskipTests package
    if %ERRORLEVEL% neq 0 (
        echo [错误] 编译失败，请检查上方报错
        pause
        exit /b 1
    )
    java -jar target\mall-server-0.1.0.jar
)

pause