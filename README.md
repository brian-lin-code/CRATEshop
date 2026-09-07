# CRATEShop 1.0.0

CRATEShop is the GUI economy shop for CRATE SMP.

## Target environment
- Java: **25**
- Server: **Purpur 26.2**
- Purpur API: **26.2.build.2632-stable**
- Build tool: Maven

Purpur's official API documentation currently lists the `26.2.build.2632-stable` API, and its Maven setup uses the Purpur snapshots repository.

## Build
From this folder, with Java 25 and Maven installed:

```bash
mvn clean package
```

The compiled plugin will be created at:

```text
target/CRATEShop-1.0.0.jar
```

## Install on Falix
1. Upload `target/CRATEShop-1.0.0.jar` to the server's `plugins` folder.
2. Make sure Vault is installed.
3. Make sure an economy provider is installed and hooked into Vault.
4. Restart the server.
5. Test with `/shop`.
