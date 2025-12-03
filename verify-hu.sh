#!/bin/bash

echo "=== VERIFICACIÓN DE LAS 5 HU ==="
echo

# 1. Verificar estructura hexagonal
echo "1. Verificando estructura hexagonal..."
if [ -d "src/main/java/com/example/hexagonalapp/domain" ] && \
   [ -d "src/main/java/com/example/hexagonalapp/application" ] && \
   [ -d "src/main/java/com/example/hexagonalapp/infrastructure" ]; then
    echo "✅ Estructura hexagonal encontrada"
else
    echo "❌ Falta estructura hexagonal"
fi

# 2. Verificar puertos
echo "2. Verificando puertos..."
if [ -d "src/main/java/com/example/hexagonalapp/domain/ports/in" ] && \
   [ -d "src/main/java/com/example/hexagonalapp/domain/ports/out" ]; then
    echo "✅ Puertos definidos"
else
    echo "❌ Puertos no encontrados"
fi

# 3. Verificar que compile
echo "3. Compilando proyecto..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "✅ Proyecto compila correctamente"
else
    echo "❌ Error de compilación"
    exit 1
fi

# 4. Verificar dependencias
echo "4. Verificando dependencias..."
if mvn dependency:tree | grep -q "spring-boot-starter-data-jpa"; then
    echo "✅ JPA configurado"
else
    echo "❌ JPA no encontrado"
fi

if mvn dependency:tree | grep -q "jjwt"; then
    echo "✅ JWT configurado"
else
    echo "❌ JWT no encontrado"
fi

if mvn dependency:tree | grep -q "spring-boot-starter-security"; then
    echo "✅ Spring Security configurado"
else
    echo "❌ Spring Security no encontrado"
fi

# 5. Verificar migraciones Flyway
echo "5. Verificando migraciones Flyway..."
if [ -d "src/main/resources/db/migration" ]; then
    COUNT=$(ls src/main/resources/db/migration/*.sql 2>/dev/null | wc -l)
    echo "✅ $COUNT migraciones encontradas"
else
    echo "❌ No hay migraciones Flyway"
fi

echo
echo "=== VERIFICACIÓN COMPLETADA ==="