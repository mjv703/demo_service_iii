package com.medicai.pillpal.service;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class GoogleAPIService {

    static void authImplicit() {
        // If you don't specify credentials when constructing the client, the client library will
        // look for credentials via the environment variable GOOGLE_APPLICATION_CREDENTIALS.
        //        Storage storage = StorageOptions.getDefaultInstance().getService();
        //
        //        System.out.println("Buckets:");
        //        Page<Bucket> buckets = storage.list();
        //        for (Bucket bucket : buckets.iterateAll()) {
        //            System.out.println(bucket.toString());
        //        }
    }

    private final ServletContext servletContext;
    private final ResourceLoader resourceLoader;
    private final CloudVisionTemplate cloudVisionTemplate;

    public GoogleAPIService(ServletContext servletContext, ResourceLoader resourceLoader, CloudVisionTemplate cloudVisionTemplate) {
        this.servletContext = servletContext;
        this.resourceLoader = resourceLoader;
        this.cloudVisionTemplate = cloudVisionTemplate;
    }

    public String extractText(String imageUrl) {
        Resource resource = resourceLoader.getResource("classpath:");

        if (resource != null) {
            String textFromImage = cloudVisionTemplate.extractTextFromImage(resource);
        } else System.out.println("NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");

        //        Resource resource = this.resourceLoader.getResource(imageUrl);
        //        try {
        //            File file = resource.getFile();
        //            if (file == null)
        //                System.out.println("NULLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
        //            String textFromImage = cloudVisionTemplate.extractTextFromImage(resource);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        // [START spring_vision_text_extraction]
        //        String textFromImage =
        //            cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));
        //        return "Text from image: " + textFromImage;
        // [END spring_vision_text_extraction]
        return "";
    }

    public void detectText() throws IOException {
        // TODO(developer): Replace these variables before running the sample.
        String filePath =
            "/media/mehdi/267b191a-c9cd-4217-aa65-b0c82965197b/Code/Amir/New_Demo/demo_service_iii/src/main/resources/images/sample.jpeg";
        detectText(filePath);
    }

    // Detects text in the specified image.
    public void detectText(String filePath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }
                System.out.format("Text: %s%n", res.getTextAnnotationsList().get(0).getDescription());
                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    //System.out.format("Text: %s%n", annotation.getDescription());
                    //System.out.format("Position : %s%n", annotation.getBoundingPoly());
                }
            }
        }
    }
}
