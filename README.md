[![Java CI with Gradle](https://github.com/eclipse-virgo/virgo-bundlor/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/eclipse-virgo/virgo-bundlor/actions/workflows/gradle-build.yml)

# Eclipse Virgo Bundlor

## Building

Please check [`BUILD.md`](BUILD.md) for instructions on how to build Bundlor.

## Contributing

Before your contribution can be accepted by the project, you need to create and electronically sign the [Eclipse Foundation Contributor License Agreement (CLA)](https://www.eclipse.org/legal/ECA.php) and sign off on the Eclipse Foundation Certificate of Origin.

For more information, please visit [Contributing to Virgo](CONTRIBUTING.md).

## License

[Eclipse Public License - v 1.0](https://www.eclipse.org/legal/epl-v10.html)

## Contact

Contact the project developers via the [Virgo Forum](https://www.eclipse.org/forums/index.php/f/159/) or the project's ["dev" mailing list](https://dev.eclipse.org/mailman/listinfo/virgo-dev).

## Archaeology

The Bundlor project uses a Git submodule to include the `virgo-build` engine
for building.  To get this submodule, run the following commands after cloning
the repository.

```shell
git submodule init
git submodule update
```
