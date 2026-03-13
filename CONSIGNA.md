## TP: Construyendo nuestra propia Blockchain P2P

### Introduccion
El objetivo es construir una red blockchain distribuida simple desde cero. La mision es programar la capa de integridad y consenso para que el libro contable sea inmutable.

### El escenario
Cada uno de los integrantes del equipo sera un nodo en la red. Si uno de ustedes mina un bloque, el resto debe recibirlo, validarlo y, si es correcto, anadirlo a su copia local del libro contable.

La evaluacion final se realizara en clase el dia 26 de marzo. Cada grupo correra su propia red privada, explicando las decisiones tomadas.

### Requerimientos
El TP se puede realizar en el lenguaje de eleccion del equipo, pero se debe validar con los profesores antes de arrancar.

Los requerimientos minimos de una blockchain son los siguientes:

1. **La funcion hash**
   La blockchain es una cadena de confianza basada en matematicas. Deben implementar el hashing de los bloques usando el algoritmo que quieran, con justificacion de la eleccion. Debe recibir lo necesario para crear un bloque y devolver el hash.

2. **Validacion de integridad**
   Cuando un par (peer) envia un bloque, no se puede confiar ciegamente. Deben programar verificaciones.

3. **El algoritmo de consenso**
   Deben definir que ocurre si reciben una cadena completa de un companero.

4. **Proof of Work**
   Para evitar que la red se llene de bloques basura (spam), hay que implementar una dificultad basica. El hash del bloque debe empezar con una cantidad de ceros definida, por ejemplo `"000"`.

5. **El bloque inicial**
   Deben definir como debe ser el bloque genesis.

### Estructura del bloque
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

### Dinamica de evaluacion
Cada grupo iniciara su red privada con un minimo de 3 integrantes. Van a tener que:

- Demostrar que la red esta funcionando con los 3 integrantes del equipo y alguno de los profesores conectados.
- Explicar las decisiones tomadas y el funcionamiento de la red distribuida.
