#### ➕ Add Jitpack Repository ❤️
```kts
maven("https://jitpack.io") {
  credentials() { username authToken }
}
```
```xml
<repository>
    <id>jitpack</id>
    <url>https://jitpack.io</url>
</repository>
<servers>
    <server>
        <id>jitpack.io</id>
        <username>${env.GITHUB_USERNAME}</username> <!-- Используйте переменную окружения для хранения имени пользователя -->
        <password>${env.GITHUB_TOKEN}</password> <!-- Используйте переменную окружения для хранения токена -->
    </server>
</servers>
```

#### ➕ Add MiniAPI to dependencies
```kts
implementation("com.github.zyr1x:MiniAPI:1.0")
```
```xml
<dependency>
    <groupId>com.github.zyr1x</groupId>
    <artifactId>MiniAPI</artifactId>
    <version>1.0</version>
</dependency>
```
