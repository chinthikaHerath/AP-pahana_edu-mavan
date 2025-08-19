package com.pahanaedu.util;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.io.ByteArrayOutputStream;

// OpenPDF imports (ALTERNATIVE - simpler and more reliable)
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Simple PDF Generator for bills using OpenPDF
 * Much simpler and more reliable than iText7
 */
public class SimplePDFGenerator {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    
    /**
     * Generate actual PDF bytes for bill using OpenPDF
     * @param bill Bill object
     * @param companyName Company name
     * @param companyAddress Company address
     * @param companyPhone Company phone
     * @return PDF as byte array
     */
    public static byte[] generateBillPDF(Bill bill, String companyName, 
                                        String companyAddress, String companyPhone) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();
            
            // Fonts
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            
            // Company Header
            Paragraph title = new Paragraph(companyName, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            Paragraph address = new Paragraph(companyAddress, normalFont);
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);
            
            Paragraph phone = new Paragraph("Tel: " + companyPhone, normalFont);
            phone.setAlignment(Element.ALIGN_CENTER);
            document.add(phone);
            
            Paragraph invoiceTitle = new Paragraph("INVOICE", headerFont);
            invoiceTitle.setAlignment(Element.ALIGN_CENTER);
            invoiceTitle.setSpacingBefore(20);
            document.add(invoiceTitle);
            
            // Bill Info Table
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(20);
            
            // Customer Info (Left)
            String customerInfo = "Bill To:\n" + 
                                bill.getCustomer().getCustomerName() + "\n" +
                                bill.getCustomer().getAddress() + "\n" +
                                (bill.getCustomer().getCity() != null ? bill.getCustomer().getCity() + "\n" : "") +
                                "Tel: " + bill.getCustomer().getTelephone();
            
            PdfPCell customerCell = new PdfPCell(new Phrase(customerInfo, normalFont));
            customerCell.setBorder(0);
            infoTable.addCell(customerCell);
            
            // Bill Details (Right)
            String billInfo = "Bill No: " + bill.getBillNumber() + "\n" +
                             "Date: " + DATE_FORMAT.format(bill.getBillDate()) + "\n" +
                             "Time: " + TIME_FORMAT.format(bill.getBillTime()) + "\n" +
                             "Account: " + bill.getCustomer().getAccountNumber() + "\n" +
                             "Payment: " + bill.getPaymentMethod();
            
            PdfPCell billCell = new PdfPCell(new Phrase(billInfo, normalFont));
            billCell.setBorder(0);
            billCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            infoTable.addCell(billCell);
            
            document.add(infoTable);
            
            // Items Table
            PdfPTable itemsTable = new PdfPTable(6);
            itemsTable.setWidthPercentage(100);
            itemsTable.setSpacingBefore(20);
            itemsTable.setWidths(new float[]{1, 2, 4, 1, 2, 2});
            
            // Headers
            itemsTable.addCell(new PdfPCell(new Phrase("#", boldFont)));
            itemsTable.addCell(new PdfPCell(new Phrase("Code", boldFont)));
            itemsTable.addCell(new PdfPCell(new Phrase("Description", boldFont)));
            itemsTable.addCell(new PdfPCell(new Phrase("Qty", boldFont)));
            itemsTable.addCell(new PdfPCell(new Phrase("Unit Price", boldFont)));
            itemsTable.addCell(new PdfPCell(new Phrase("Total", boldFont)));
            
            // Items
            int count = 1;
            for (BillItem item : bill.getBillItems()) {
                itemsTable.addCell(new PdfPCell(new Phrase(String.valueOf(count++), normalFont)));
                itemsTable.addCell(new PdfPCell(new Phrase(getItemCode(item), normalFont)));
                itemsTable.addCell(new PdfPCell(new Phrase(getItemName(item), normalFont)));
                itemsTable.addCell(new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), normalFont)));
                itemsTable.addCell(new PdfPCell(new Phrase(DECIMAL_FORMAT.format(item.getUnitPrice()), normalFont)));
                itemsTable.addCell(new PdfPCell(new Phrase(DECIMAL_FORMAT.format(item.getTotalPrice()), normalFont)));
            }
            
            document.add(itemsTable);
            
            // Summary Table
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            summaryTable.setSpacingBefore(20);
            
            summaryTable.addCell(new PdfPCell(new Phrase("Subtotal:", normalFont)));
            summaryTable.addCell(new PdfPCell(new Phrase(DECIMAL_FORMAT.format(bill.getSubtotal()), normalFont)));
            
            if (bill.getDiscountAmount() > 0) {
                summaryTable.addCell(new PdfPCell(new Phrase("Discount:", normalFont)));
                summaryTable.addCell(new PdfPCell(new Phrase("-" + DECIMAL_FORMAT.format(bill.getDiscountAmount()), normalFont)));
            }
            
            if (bill.getTaxAmount() > 0) {
                summaryTable.addCell(new PdfPCell(new Phrase("Tax:", normalFont)));
                summaryTable.addCell(new PdfPCell(new Phrase("+" + DECIMAL_FORMAT.format(bill.getTaxAmount()), normalFont)));
            }
            
            summaryTable.addCell(new PdfPCell(new Phrase("TOTAL:", boldFont)));
            summaryTable.addCell(new PdfPCell(new Phrase("LKR " + DECIMAL_FORMAT.format(bill.getTotalAmount()), boldFont)));
            
            document.add(summaryTable);
            
            // Footer
            Paragraph footer = new Paragraph("Thank you for your business!", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);
            
            document.close();
            return baos.toByteArray();
            
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Generate HTML content for bill that can be printed as PDF
     * @param bill Bill object
     * @param companyName Company name
     * @param companyAddress Company address
     * @param companyPhone Company phone
     * @return HTML string
     */
    public static String generateBillHTML(Bill bill, String companyName, 
                                         String companyAddress, String companyPhone) {
        StringBuilder html = new StringBuilder();
        
        // Start HTML
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Bill #").append(bill.getBillNumber()).append("</title>");
        html.append(getInlineStyles());
        html.append("</head>");
        html.append("<body>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<h1>").append(companyName).append("</h1>");
        html.append("<p>").append(companyAddress).append("</p>");
        html.append("<p>Tel: ").append(companyPhone).append("</p>");
        html.append("<h2>INVOICE</h2>");
        html.append("<p>Bill No: ").append(bill.getBillNumber()).append("</p>");
        html.append("</div>");
        
        // Customer Info
        html.append("<div class='info'>");
        html.append("<div class='customer'>");
        html.append("<h3>Bill To:</h3>");
        html.append("<p><strong>").append(bill.getCustomer().getCustomerName()).append("</strong></p>");
        html.append("<p>").append(bill.getCustomer().getAddress()).append("</p>");
        if (bill.getCustomer().getCity() != null) {
            html.append("<p>").append(bill.getCustomer().getCity()).append("</p>");
        }
        html.append("<p>Tel: ").append(bill.getCustomer().getTelephone()).append("</p>");
        html.append("</div>");
        
        html.append("<div class='details'>");
        html.append("<p><strong>Date:</strong> ").append(DATE_FORMAT.format(bill.getBillDate())).append("</p>");
        html.append("<p><strong>Time:</strong> ").append(TIME_FORMAT.format(bill.getBillTime())).append("</p>");
        html.append("<p><strong>Account:</strong> ").append(bill.getCustomer().getAccountNumber()).append("</p>");
        html.append("<p><strong>Payment:</strong> ").append(bill.getPaymentMethod()).append("</p>");
        html.append("</div>");
        html.append("</div>");
        
        // Items Table
        html.append("<table class='items'>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>#</th>");
        html.append("<th>Item Code</th>");
        html.append("<th>Description</th>");
        html.append("<th>Qty</th>");
        html.append("<th>Unit Price</th>");
        html.append("<th>Total</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
        
        int count = 1;
        for (BillItem item : bill.getBillItems()) {
            html.append("<tr>");
            html.append("<td>").append(count++).append("</td>");
            html.append("<td>").append(getItemCode(item)).append("</td>");
            html.append("<td>").append(getItemName(item)).append("</td>");
            html.append("<td class='center'>").append(item.getQuantity()).append("</td>");
            html.append("<td class='right'>").append(DECIMAL_FORMAT.format(item.getUnitPrice())).append("</td>");
            html.append("<td class='right'>").append(DECIMAL_FORMAT.format(item.getTotalPrice())).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</tbody>");
        html.append("</table>");
        
        // Summary
        html.append("<div class='summary'>");
        html.append("<table>");
        html.append("<tr><td>Subtotal:</td><td>").append(DECIMAL_FORMAT.format(bill.getSubtotal())).append("</td></tr>");
        if (bill.getDiscountAmount() > 0) {
            html.append("<tr><td>Discount:</td><td>-").append(DECIMAL_FORMAT.format(bill.getDiscountAmount())).append("</td></tr>");
        }
        if (bill.getTaxAmount() > 0) {
            html.append("<tr><td>Tax:</td><td>+").append(DECIMAL_FORMAT.format(bill.getTaxAmount())).append("</td></tr>");
        }
        html.append("<tr class='total'><td><strong>Total:</strong></td><td><strong>LKR ").append(DECIMAL_FORMAT.format(bill.getTotalAmount())).append("</strong></td></tr>");
        html.append("</table>");
        html.append("</div>");
        
        // Footer
        html.append("<div class='footer'>");
        html.append("<p class='thank-you'>Thank you for your business!</p>");
        html.append("<div class='signatures'>");
        html.append("<div class='signature'>");
        html.append("<p>_____________________</p>");
        html.append("<p>Authorized Signature</p>");
        html.append("</div>");
        html.append("<div class='signature'>");
        html.append("<p>_____________________</p>");
        html.append("<p>Customer Signature</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
    
    /**
     * Safely get item code from BillItem
     */
    private static String getItemCode(BillItem billItem) {
        if (billItem.getItem() != null && billItem.getItem().getItemCode() != null) {
            return billItem.getItem().getItemCode();
        }
        if (billItem.getItemCode() != null) {
            return billItem.getItemCode();
        }
        return "ITEM-" + billItem.getItemId(); // Fallback
    }

    /**
     * Safely get item name from BillItem
     */
    private static String getItemName(BillItem billItem) {
        if (billItem.getItem() != null && billItem.getItem().getItemName() != null) {
            return billItem.getItem().getItemName();
        }
        if (billItem.getItemName() != null) {
            return billItem.getItemName();
        }
        return "Unknown Item"; // Fallback
    }
    
    /**
     * Get inline styles for the HTML
     */
    private static String getInlineStyles() {
        return "<style>" +
            "body { font-family: Arial, sans-serif; font-size: 12pt; margin: 20px; }" +
            ".header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 20px; margin-bottom: 20px; }" +
            ".header h1 { margin: 0; color: #2c3e50; }" +
            ".header h2 { margin: 10px 0; }" +
            ".info { display: flex; justify-content: space-between; margin-bottom: 30px; }" +
            ".customer { flex: 1; }" +
            ".details { text-align: right; }" +
            ".items { width: 100%; border-collapse: collapse; margin-bottom: 30px; }" +
            ".items th { background: #2c3e50; color: white; padding: 10px; text-align: left; }" +
            ".items td { padding: 8px; border-bottom: 1px solid #ddd; }" +
            ".center { text-align: center; }" +
            ".right { text-align: right; }" +
            ".summary { text-align: right; margin-bottom: 30px; }" +
            ".summary table { margin-left: auto; }" +
            ".summary td { padding: 5px 10px; }" +
            ".total { border-top: 2px solid #333; font-size: 14pt; }" +
            ".footer { border-top: 1px solid #ddd; padding-top: 20px; }" +
            ".thank-you { text-align: center; font-size: 14pt; font-style: italic; margin-bottom: 30px; }" +
            ".signatures { display: flex; justify-content: space-around; }" +
            ".signature { text-align: center; }" +
            "@media print { body { margin: 0; } }" +
            "</style>";
    }
    
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }
    
    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}