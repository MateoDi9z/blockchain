package api.entities

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

fun createSignature(
    privateKey: PrivateKey,
    message: String,
): String {
    val s = Signature.getInstance("SHA256withECDSA")
    s.initSign(privateKey)
    s.update(message.toByteArray())
    val signatureBytes = s.sign()
    return Base64.getEncoder().encodeToString(signatureBytes)
}

fun verifySignature(
    publicKey: PublicKey,
    message: String,
    signature: String,
): Boolean {
    val s = Signature.getInstance("SHA256withECDSA")
    s.initVerify(publicKey)
    s.update(message.toByteArray())
    val signatureBytes = Base64.getDecoder().decode(signature)
    return s.verify(signatureBytes)
}

fun getPublicKeyFromString(keyString: String): PublicKey {
    val publicBytes = Base64.getDecoder().decode(keyString)
    val keySpec = X509EncodedKeySpec(publicBytes)
    val keyFactory = KeyFactory.getInstance("EC")
    return keyFactory.generatePublic(keySpec)
}
