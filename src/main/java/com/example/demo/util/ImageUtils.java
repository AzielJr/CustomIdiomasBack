package com.example.demo.util;

import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.regex.Pattern;

@Component
public class ImageUtils {
    
    // Padrão para validar base64 de imagem
    private static final Pattern BASE64_IMAGE_PATTERN = Pattern.compile(
        "^data:image/(jpeg|jpg|png|gif|bmp|webp);base64,([A-Za-z0-9+/=]+)$"
    );
    
    // Tamanho máximo da imagem em bytes (5MB)
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
    
    /**
     * Converte uma string base64 para array de bytes
     * @param base64String String base64 da imagem (com ou sem prefixo data:image)
     * @return Array de bytes da imagem
     * @throws IllegalArgumentException se a string base64 for inválida
     */
    public byte[] base64ToByteArray(String base64String) {
        if (base64String == null || base64String.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Remove o prefixo data:image se presente
            String base64Data = extractBase64Data(base64String);
            
            // Decodifica o base64
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            
            // Valida o tamanho da imagem
            if (imageBytes.length > MAX_IMAGE_SIZE) {
                throw new IllegalArgumentException(
                    String.format("Imagem muito grande. Tamanho máximo permitido: %d bytes", MAX_IMAGE_SIZE)
                );
            }
            
            return imageBytes;
            
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("String base64 inválida: " + e.getMessage());
        }
    }
    
    /**
     * Converte array de bytes para string base64
     * @param imageBytes Array de bytes da imagem
     * @param mimeType Tipo MIME da imagem (ex: "image/jpeg")
     * @return String base64 com prefixo data:image
     */
    public String byteArrayToBase64(byte[] imageBytes, String mimeType) {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        
        String base64 = Base64.getEncoder().encodeToString(imageBytes);
        return String.format("data:%s;base64,%s", mimeType != null ? mimeType : "image/jpeg", base64);
    }
    
    /**
     * Valida se a string base64 representa uma imagem válida
     * @param base64String String base64 a ser validada
     * @return true se válida, false caso contrário
     */
    public boolean isValidBase64Image(String base64String) {
        if (base64String == null || base64String.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Verifica se tem o formato correto
            if (!BASE64_IMAGE_PATTERN.matcher(base64String).matches()) {
                return false;
            }
            
            // Tenta decodificar para verificar se é base64 válido
            String base64Data = extractBase64Data(base64String);
            byte[] decoded = Base64.getDecoder().decode(base64Data);
            
            // Verifica tamanho
            return decoded.length <= MAX_IMAGE_SIZE && decoded.length > 0;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extrai o tipo MIME da string base64
     * @param base64String String base64 com prefixo
     * @return Tipo MIME (ex: "image/jpeg") ou null se não encontrado
     */
    public String extractMimeType(String base64String) {
        if (base64String == null || !base64String.startsWith("data:image/")) {
            return null;
        }
        
        int semicolonIndex = base64String.indexOf(';');
        if (semicolonIndex > 0) {
            return base64String.substring(5, semicolonIndex); // Remove "data:" do início
        }
        
        return null;
    }
    
    /**
     * Extrai apenas os dados base64, removendo o prefixo data:image
     * @param base64String String base64 completa
     * @return Apenas os dados base64
     */
    private String extractBase64Data(String base64String) {
        if (base64String.startsWith("data:image/")) {
            int commaIndex = base64String.indexOf(',');
            if (commaIndex > 0) {
                return base64String.substring(commaIndex + 1);
            }
        }
        
        // Se não tem prefixo, assume que já é apenas base64
        return base64String;
    }
    
    /**
     * Obtém informações sobre a imagem base64
     * @param base64String String base64 da imagem
     * @return Map com informações da imagem
     */
    public ImageInfo getImageInfo(String base64String) {
        if (!isValidBase64Image(base64String)) {
            return null;
        }
        
        try {
            String mimeType = extractMimeType(base64String);
            byte[] imageBytes = base64ToByteArray(base64String);
            
            return ImageInfo.builder()
                .mimeType(mimeType)
                .sizeInBytes(imageBytes.length)
                .isValid(true)
                .build();
                
        } catch (Exception e) {
            return ImageInfo.builder()
                .isValid(false)
                .error(e.getMessage())
                .build();
        }
    }
    
    /**
     * Classe para informações da imagem
     */
    public static class ImageInfo {
        private String mimeType;
        private long sizeInBytes;
        private boolean isValid;
        private String error;
        
        private ImageInfo(Builder builder) {
            this.mimeType = builder.mimeType;
            this.sizeInBytes = builder.sizeInBytes;
            this.isValid = builder.isValid;
            this.error = builder.error;
        }
        
        public static Builder builder() {
            return new Builder();
        }
        
        // Getters
        public String getMimeType() { return mimeType; }
        public long getSizeInBytes() { return sizeInBytes; }
        public boolean isValid() { return isValid; }
        public String getError() { return error; }
        
        public static class Builder {
            private String mimeType;
            private long sizeInBytes;
            private boolean isValid;
            private String error;
            
            public Builder mimeType(String mimeType) {
                this.mimeType = mimeType;
                return this;
            }
            
            public Builder sizeInBytes(long sizeInBytes) {
                this.sizeInBytes = sizeInBytes;
                return this;
            }
            
            public Builder isValid(boolean isValid) {
                this.isValid = isValid;
                return this;
            }
            
            public Builder error(String error) {
                this.error = error;
                return this;
            }
            
            public ImageInfo build() {
                return new ImageInfo(this);
            }
        }
    }
}