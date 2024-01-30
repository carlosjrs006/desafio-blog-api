package br.com.blog.service.impl;

import br.com.blog.dtos.forms.FileResponseDTO;
import br.com.blog.service.StorageCloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileServiceIpml {

    @Autowired
    private StorageCloudinaryService storageCloudinaryService;


    public List<FileResponseDTO> salvarImagemNaClaud(List<MultipartFile> files) throws IOException {
        List<FileResponseDTO> fileResponseDTOList = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                if (bufferedImage == null) {
                    throw new RuntimeException("Imagem inválida!!");
                }

                Map<String, String> map = storageCloudinaryService.upload(file);

                FileResponseDTO fileResponseDTO = new FileResponseDTO();
                fileResponseDTO.setPublicId(map.get("public_id"));
                fileResponseDTO.setUrl(map.get("url"));
                fileResponseDTO.setNome(map.get("original_filename"));

                fileResponseDTOList.add(fileResponseDTO);
            }
            return fileResponseDTOList;
        }  catch (Exception ex) {
            throw new RuntimeException("Error ao tentar salvar imagem, por favor tente novamente!", ex);
        }
    }
}
