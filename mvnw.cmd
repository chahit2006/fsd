@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF)
@REM Maven Wrapper startup batch script
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0__%"=="" SET __MVNW_ARG0__=%~dpnx0

@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=
@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $wrapperJar=$scriptDir+'.mvn\wrapper\maven-wrapper.jar'; $wrapperUrl='https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar'; $wrapperProperties=$scriptDir+'.mvn\wrapper\maven-wrapper.properties'; if(Test-Path $wrapperProperties){$props=[IO.File]::ReadAllText($wrapperProperties); if($props -match 'wrapperUrl\s*=\s*(\S+)'){$wrapperUrl=$matches[1]}}; if(!(Test-Path $wrapperJar)){Write-Host 'Downloading Maven wrapper...'; (New-Object Net.WebClient).DownloadFile($wrapperUrl,$wrapperJar)}; Write-Output ('MAVEN_WRAPPER_JAR='+$wrapperJar)}"`) DO @(
    IF "%%A"=="MAVEN_WRAPPER_JAR" SET MVNW_WRAPPERJAR=%%B
)
@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%

@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET MVNW_VERBOSE=false

"%JAVA_HOME%\bin\java.exe" %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath "%MVNW_WRAPPERJAR%" "-Dmaven.multiModuleProjectDirectory=%~dp0." %WRAPPER_LAUNCHER% %*
if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

cmd /C exit /B %ERROR_CODE%
