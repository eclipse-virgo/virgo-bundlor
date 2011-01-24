#!/bin/bash

rm -rf target
mkdir target

../../../bin/bundlor.sh \
	-i ../ivy-cache/repository/org.springframework.integration/org.springframework.integration/1.0.1.RELEASE/org.springframework.integration-1.0.1.RELEASE.jar \
	-m ./template.mf \
	-o ./target/org.springframework.integration.jar
