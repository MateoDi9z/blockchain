# Blockchain P2P

Una blockchain hecha desde cero en **Kotlin**, pensada como trabajo practico para entender de verdad que pasa debajo del humo: hashes, integridad, Merkle trees, mineria, consenso y sincronizacion entre nodos.

Este repo no busca "usar blockchain". Busca **construir una**.

La consigna completa del TP esta en [`CONSIGNA.md`](./CONSIGNA.md).

## La idea
El objetivo del proyecto es levantar una red privada entre integrantes del equipo donde cada persona corre un nodo. Cuando un nodo mina un bloque, el resto lo recibe, lo valida y decide si lo incorpora a su copia local de la cadena.

En otras palabras:

- cada nodo mantiene su propio libro contable
- los bloques no se aceptan por confianza, sino por verificaciones
- la red necesita un criterio comun para elegir la cadena valida
- minar debe costar trabajo para evitar spam

## Estado actual del proyecto
Hoy el proyecto ya tiene implementadas varias piezas base sobre las que se puede construir la red:

- `SHA-256` para hashing
- `Transaction` con serializacion deterministica y monto fijo de 6 decimales
- `MerkleTree` para resumir transacciones en una raiz unica
- `Block` como estructura inicial del bloque
- tests unitarios para hashing, transacciones y Merkle tree

Todavia faltan las partes mas "blockchain de verdad":

- hash completo del bloque
- bloque genesis
- validacion de bloques recibidos desde peers
- cadena completa y reglas de integridad
- Proof of Work con dificultad
- consenso entre nodos
- networking P2P para propagar bloques/cadenas

## Como funciona la blockchain que queremos construir
La arquitectura esperada del proyecto se puede pensar en 6 pasos:

### 1. Crear transacciones
Las transacciones representan transferencias entre participantes. En este repo ya existe una entidad `Transaction` con:

- emisor
- receptor
- monto
- timestamp

Cada transaccion puede serializarse de forma deterministica y hashearse. Eso es importante porque si dos nodos ven exactamente la misma transaccion, deben obtener exactamente el mismo hash.

### 2. Agruparlas en un bloque
Un bloque junta un conjunto de transacciones y agrega metadatos que lo conectan con el bloque anterior:

- indice
- timestamp
- `previousHash`
- `nonce`
- hash propio

Ademas, las transacciones se resumen en una **Merkle root**, que permite detectar cambios en el contenido del bloque sin tener que rehacer comparaciones transaccion por transaccion.

### 3. Calcular el hash del bloque
El hash del bloque debe salir de una serializacion estable de sus campos. Si cambia un dato, aunque sea minimo, el hash debe cambiar por completo.

Ese hash es lo que vuelve a la cadena sensible a cualquier alteracion:

- si se modifica una transaccion
- cambia la Merkle root
- cambia el hash del bloque
- se rompe el enlace con el siguiente bloque

### 4. Minar con Proof of Work
Para agregar un bloque nuevo, el nodo minero debera probar distintos valores de `nonce` hasta encontrar un hash que cumpla una dificultad, por ejemplo:

```text
000ab39f...
```

Eso hace que crear bloques tenga un costo computacional. La idea no es competir con Bitcoin, sino simular el mecanismo que evita que cualquiera inunde la red con bloques invalidos o basura.

### 5. Validar antes de aceptar
Si un peer envia un bloque, los demas nodos no lo agregan automaticamente. Primero deberian verificar:

- que el `previousHash` apunte al ultimo bloque conocido
- que el hash del bloque sea correcto
- que la Merkle root coincida con las transacciones
- que el bloque cumpla la dificultad
- que el indice y timestamps tengan sentido

Si alguna de esas condiciones falla, el bloque se rechaza.

### 6. Resolver conflictos con consenso
Si dos nodos tienen cadenas distintas, hace falta una regla para decidir cual conservar. La estrategia mas simple para este TP suele ser:

- quedarse con la cadena valida mas larga

Tambien puede justificarse una variante basada en mayor trabajo acumulado, que conceptualmente es mas cercana a PoW real.

## Estructura conceptual del bloque
La consigna propone una estructura parecida a esta:

```json
{
  "index": 1,
  "timestamp": 1700000000,
  "transactions": [
    {
      "from": "0xRocio",
      "to": "0xPedro",
      "amount": 100,
      "sig": "0x...."
    }
  ],
  "previousHash": "0x0081c4...",
  "hash": "0x00a3f2...",
  "nonce": 4253
}
```

En el repo actual ya aparece parte de esa idea, aunque la estructura final del bloque todavia tiene que evolucionar para incorporar todo lo necesario para mineria, validacion y sincronizacion entre nodos.

## Componentes del repo
Resumen rapido de las piezas actuales:

- `src/main/kotlin/crypto/hash/Hash.kt`: utilitario de hashing con `SHA-256`
- `src/main/kotlin/core/transaction/Transaction.kt`: modelo de transaccion y hash propio
- `src/main/kotlin/crypto/merkle/MerkleTree.kt`: calculo de Merkle root
- `src/main/kotlin/core/block/Block.kt`: estructura inicial del bloque
- `src/test/kotlin/...`: tests unitarios de los componentes base

## Por que Kotlin
Elegir Kotlin para este TP tiene varias ventajas:

- sintaxis concisa y expresiva
- buen modelado de datos con `data class`
- excelente tooling con Gradle y JUnit
- interoperabilidad con el ecosistema Java

No es el lenguaje tipico de tutorial de blockchain, y justamente por eso tambien esta bueno: obliga a entender los conceptos en lugar de copiar una implementacion de internet.

## Como correr el proyecto
### Requisitos

- Java 23
- Gradle wrapper incluido en el repo

### Comandos utiles
```bash
./gradlew test
./gradlew run
```

> Nota: hoy `Main.kt` sigue siendo el template inicial. El valor real del repo esta en las estructuras base y sus tests; la parte de nodo blockchain todavia esta en construccion.

## Roadmap sugerido
Un camino razonable para completar el TP podria ser:

1. Definir bien el bloque final y el bloque genesis.
2. Implementar el hash del bloque con serializacion deterministica.
3. Agregar mineria con `nonce` y dificultad configurable.
4. Crear una `Blockchain` que valide y agregue bloques.
5. Implementar consenso para resolver forks o cadenas competidoras.
6. Sumar comunicacion P2P entre nodos para propagar bloques y cadenas.
7. Preparar una demo con al menos 3 nodos reales corriendo en red privada.

## Que deberia poder demostrar la version final
Si el proyecto queda completo, la demo ideal deberia mostrar algo asi:

- se levantan 3 nodos
- uno mina un bloque nuevo
- los otros lo reciben por red
- validan hash, enlace y dificultad
- actualizan su copia local de la cadena
- ante conflicto, todos convergen a la misma version valida

## Cierre
Este proyecto apunta a construir una blockchain educativa, chica y entendible, pero con las ideas centrales del problema real:

- inmutabilidad
- verificabilidad
- costo de minado
- consenso distribuido

La meta no es hacer "la proxima gran cripto". La meta es salir entendiendo por que una blockchain funciona cuando funciona, y por que se rompe cuando esas reglas no estan bien definidas.
