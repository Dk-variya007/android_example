package com.example.apicallingapp;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfGenActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 100;
    private EditText titleEditText, descriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_gen);

        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        Button generatePdfButton = findViewById(R.id.generatePdfButton);

        // Check for storage permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }

        generatePdfButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please enter both title and description", Toast.LENGTH_SHORT).show();
            } else {
                generatePDF(title, description);
            }
        });
    }

    private void generatePDF(String title, String description) {
        // Define the app-specific storage directory for PDFs
        File pdfFolder = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "MyPDFs");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir(); // Create the directory if it doesn't exist
        }

        String fileName = "GeneratedPDF_" + System.currentTimeMillis() + ".pdf"; // Unique file name
        File pdfFile = new File(pdfFolder, fileName);

        try {
            // Create a PdfWriter instance
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));

            // Create a Document instance
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            // Add title and description to the document
            document.add(new Paragraph("Title: " + title).setBold());
            document.add(new Paragraph("\nDescription:\n" + description));

            // Close the document
            document.close();

            Toast.makeText(this, "PDF generated at: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.e("PDF Path", "PDF generated at: " + pdfFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}