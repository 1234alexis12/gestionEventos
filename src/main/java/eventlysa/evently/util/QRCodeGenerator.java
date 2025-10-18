package eventlysa.evently.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component; // Para que Spring lo gestione

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Component // Marcamos como componente de Spring para poder inyectarlo
public class QRCodeGenerator {

    /**
     * Genera una imagen de código QR para el texto dado.
     *
     * @param text El texto a codificar en el QR (ej. el codigo_qr de la entrada).
     * @param width El ancho de la imagen QR en píxeles.
     * @param height El alto de la imagen QR en píxeles.
     * @return La imagen QR como un String en formato Base64, lista para usar en HTML.
     * @throws WriterException Si ocurre un error durante la generación del QR.
     * @throws IOException Si ocurre un error al escribir la imagen en memoria.
     */
    public String generateQRCodeImageBase64(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Escribir la imagen en un flujo de bytes en memoria
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        
        // Convertir los bytes de la imagen a Base64 String
        byte[] pngData = pngOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pngData);
    }

    /**
     * Genera un String único que puede ser usado como codigo_qr para una entrada.
     * Combina un prefijo, ID de evento, ID de usuario y timestamp.
     * ¡NOTA: Esto es un ejemplo simple, podrías necesitar algo más robusto/seguro!
     * * @param eventoId ID del evento.
     * @param usuarioId ID del usuario.
     * @return Un String único para el codigo_qr.
     */
     public String generateUniqueQRCodeText(Long eventoId, Long usuarioId) {
         // Ejemplo: QR-E1-U3-1678886400000 (Prefijo-EventoID-UsuarioID-Timestamp)
         long timestamp = System.currentTimeMillis();
         return "QR-E" + eventoId + "-U" + usuarioId + "-" + timestamp;
     }

}