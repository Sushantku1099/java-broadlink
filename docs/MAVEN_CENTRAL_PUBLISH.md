# Step 2: Maven Central Publishing Guide

## What you get after this

```xml
<!-- Anyone in the world can add this to pom.xml -->
<dependency>
    <groupId>io.github.sushantku1099</groupId>
    <artifactId>java-broadlink</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## Complete Step-by-Step (2 hours total)

### 1. Generate GPG Key (15 min)

```bash
# Linux
sudo apt install gnupg
gpg --gen-key
# Name: Sushant Sagar
# Email: sushant1892004@gmail.com

# Check your key
gpg --list-keys

# Publish to keyservers
gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
gpg --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
```

---

### 2. Register on Maven Central (10 min)

1. Go to **[central.sonatype.com](https://central.sonatype.com/)**
2. Click **Sign In** → Login with **GitHub**
3. Authorize Sonatype
4. Click your profile → **Publish** tab
5. Click **Register Namespace**
6. Enter: `io.github.sushantku1099`
7. Sonatype verifies you own `github.com/Sushantku1099`
8. Done. Namespace is yours forever.

---

### 3. Generate User Token (5 min)

1. central.sonatype.com → Profile → **User Token**
2. Click **Generate User Token**
3. Copy the XML snippet

---

### 4. Configure Maven Settings (5 min)

```bash
mkdir -p ~/.m2
nano ~/.m2/settings.xml
```

Paste this:

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_TOKEN_USERNAME</username>
      <password>YOUR_TOKEN_PASSWORD</password>
    </server>
    <server>
      <id>gpg.passphrase</id>
      <passphrase>YOUR_GPG_PASSPHRASE</passphrase>
    </server>
  </servers>
</settings>
```

---

### 5. Deploy to Maven Central (5 min)

```bash
cd /home/user/java-broadlink
mvn clean deploy -P release

# This does:
# ✅ Runs 53 tests
# ✅ Builds JAR + sources JAR + javadoc JAR
# ✅ GPG-signs all 3 JARs
# ✅ Uploads to Maven Central
# ✅ Auto-publishes (live in 10-30 min)
```

---

### 6. Verify Live

Go to: **https://central.sonatype.com/artifact/io.github.sushantku1099/java-broadlink**

---

### 7. Add Badge to README

```markdown
[![Maven Central](https://img.shields.io/maven-central/v/io.github.sushantku1099/java-broadlink)](https://central.sonatype.com/artifact/io.github.sushantku1099/java-broadlink)
```

---

## Future Releases

```bash
# Update version in pom.xml: 1.0.0 → 1.1.0
mvn versions:set -DnewVersion=1.1.0

# Deploy
mvn clean deploy -P release

# Tag and push
git tag v1.1.0
git push origin v1.1.0
```
