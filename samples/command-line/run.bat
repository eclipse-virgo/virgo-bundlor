@ECHO OFF

IF EXIST target rmdir /S /Q target
mkdir target

..\..\..\bin\bundlor.bat -i ../ivy-cache/repository/org.springframework.integration/org.springframework.integration/1.0.1.RELEASE/org.springframework.integration-1.0.1.RELEASE.jar -m template.mf -o target/org.springframework.integration.jar
