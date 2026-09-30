import os

filepath = 'app/build.gradle.kts'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Make changes to the content using string replace or regex
content = content.replace(
    'android {\n    namespace = "com.volunteernews24.app"\n    compileSdk = 35',
    'android {\n    namespace = "com.volunteernews24.app"\n    compileSdk = 35\n\n    val properties = java.util.Properties()\n    val localPropertiesFile = rootProject.file("local.properties")\n    if (localPropertiesFile.exists()) {\n        properties.load(java.io.FileInputStream(localPropertiesFile))\n    }\n    val mapsApiKey = properties.getProperty("MAPS_API_KEY") ?: ""'
)
content = content.replace(
    'android {\r\n    namespace = "com.volunteernews24.app"\r\n    compileSdk = 35',
    'android {\n    namespace = "com.volunteernews24.app"\n    compileSdk = 35\n\n    val properties = java.util.Properties()\n    val localPropertiesFile = rootProject.file("local.properties")\n    if (localPropertiesFile.exists()) {\n        properties.load(java.io.FileInputStream(localPropertiesFile))\n    }\n    val mapsApiKey = properties.getProperty("MAPS_API_KEY") ?: ""'
)

content = content.replace(
    '        vectorDrawables {\n            useSupportLibrary = true\n        }\n    }',
    '        vectorDrawables {\n            useSupportLibrary = true\n        }\n        buildConfigField("String", "MAPS_API_KEY", "\\"$mapsApiKey\\"")\n    }'
)
content = content.replace(
    '        vectorDrawables {\r\n            useSupportLibrary = true\r\n        }\r\n    }',
    '        vectorDrawables {\n            useSupportLibrary = true\n        }\n        buildConfigField("String", "MAPS_API_KEY", "\\"$mapsApiKey\\"")\n    }'
)

content = content.replace(
    '    buildFeatures {\n        compose = true\n    }',
    '    buildFeatures {\n        compose = true\n        buildConfig = true\n    }'
)
content = content.replace(
    '    buildFeatures {\r\n        compose = true\r\n    }',
    '    buildFeatures {\n        compose = true\n        buildConfig = true\n    }'
)

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)

print("Done")
