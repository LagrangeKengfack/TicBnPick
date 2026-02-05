/**
 * @file services/pdfService.ts
 * @description Generates a professional delivery receipt matching the official design.
 */
import jsPDF from "jspdf";
import OriginalQRCode from "qrcode";

const APP_NAME = "TickBnPick";
const BRAND_ORANGE = [249, 115, 22]; // RGB for #F97316

export const pdfService = {
  generateBordereauPDF: async (
    allData: any,
    trackingNumber: string,
    totalPrice: number,
    operatorFee: number,
    selectedMethod: string,
  ) => {
    try {
      const pdf = new jsPDF("p", "mm", "a4");
      const pageWidth = pdf.internal.pageSize.getWidth();
      const margin = 15;
      let y = 20;

      // --- HELPER FUNCTIONS ---
      const addSectionTitle = (title: string) => {
        pdf.setFontSize(12);
        pdf.setFont("helvetica", "bold");
        pdf.setTextColor(45, 55, 72);
        pdf.text(title, margin, y);
        y += 2;
        pdf.setDrawColor(45, 55, 72);
        pdf.setLineWidth(0.5);
        pdf.line(margin, y, pageWidth - margin, y);
        y += 8;
      };

      const addGridField = (
        label: string,
        value: string,
        x: number,
        yPos: number,
      ) => {
        pdf.setFontSize(9);
        pdf.setFont("helvetica", "bold");
        pdf.setTextColor(0, 0, 0);
        pdf.text(`${label}:`, x, yPos);

        pdf.setFont("helvetica", "normal");
        pdf.setTextColor(60, 60, 60);
        const valueX = x + pdf.getTextWidth(`${label}: `) + 1;
        pdf.text(String(value || "N/A"), valueX, yPos);
      };

      // --- 1. HEADER (Logo & QR Code) ---
      // Logo (Text based)
      pdf.setFontSize(22);
      pdf.setFont("helvetica", "bold");
      pdf.setTextColor(BRAND_ORANGE[0], BRAND_ORANGE[1], BRAND_ORANGE[2]);
      pdf.text(APP_NAME, margin, y);

      // Slogan
      y += 6;
      pdf.setFontSize(9);
      pdf.setFont("helvetica", "normal");
      pdf.setTextColor(120, 120, 120);
      pdf.text("Votre solution de livraison de confiance", margin, y);

      // QR Code (Top Right)
      const qrDataURL = await OriginalQRCode.toDataURL(trackingNumber, {
        margin: 1,
      });
      const qrSize = 30;
      pdf.addImage(
        qrDataURL,
        "PNG",
        pageWidth - margin - qrSize,
        12,
        qrSize,
        qrSize,
      );

      // Receipt Title & Meta
      pdf.setTextColor(0, 0, 0);
      pdf.setFont("helvetica", "bold");
      pdf.setFontSize(11);
      pdf.text("Bordereau d'Expédition", pageWidth - margin - 32, 17, {
        align: "right",
      });

      pdf.setFontSize(8);
      pdf.setFont("helvetica", "normal");
      pdf.text(`N°: ${trackingNumber}`, pageWidth - margin - 32, 21, {
        align: "right",
      });
      pdf.text(
        `Date: ${new Date().toLocaleDateString("fr-FR")}`,
        pageWidth - margin - 32,
        25,
        { align: "right" },
      );

      y += 25;

      // --- 2. INTERVENANTS (Two Columns) ---
      addSectionTitle("Intervenants");
      const startYInter = y;

      // Column 1: Sender
      addGridField("Expéditeur", allData.senderName, margin, y);
      y += 6;
      addGridField("Téléphone", allData.senderPhone, margin, y);
      y += 6;
      addGridField("Dépôt", allData.departurePointName, margin, y);

      // Column 2: Recipient
      let yCol2 = startYInter;
      const col2X = pageWidth / 2 + 5;
      addGridField("Destinataire", allData.recipientName, col2X, yCol2);
      yCol2 += 6;
      addGridField("Téléphone", allData.recipientPhone, col2X, yCol2);
      yCol2 += 6;
      addGridField("Point de Retrait", allData.arrivalPointName, col2X, yCol2);

      y = Math.max(y, yCol2) + 15;

      // --- 3. DÉTAILS DU COLIS ---
      addSectionTitle("Détails du colis");
      const startYPackage = y;

      addGridField("Désignation", allData.designation, margin, y);
      y += 6;
      addGridField("Poids", `${allData.weight} kg`, margin, y);
      y += 6;

      let caracteristiques = [];
      if (allData.isFragile) caracteristiques.push("Fragile");
      if (allData.isPerishable) caracteristiques.push("Périssable");
      if (allData.isInsured)
        caracteristiques.push(
          `Assuré (Valeur: ${allData.declaredValue || 0} FCFA)`,
        );
      addGridField(
        "Spécificités",
        caracteristiques.join(", ") || "Aucune",
        margin,
        y,
      );

      // Package Image (Right Side)
      if (allData.photo) {
        try {
          // Calculate aspect ratio for 40mm width
          pdf.addImage(
            allData.photo,
            "JPEG",
            pageWidth - margin - 50,
            startYPackage - 5,
            50,
            40,
          );
        } catch (e) {
          console.error("PDF Image error", e);
        }
      }

      y = startYPackage + 45;

      // --- 4. RÉCAPITULATIF FINANCIER ---
      addSectionTitle("Récapitulatif Financier");

      addGridField(
        "Coût de base",
        `${allData.basePrice.toLocaleString()} FCFA`,
        margin,
        y,
      );
      y += 6;
      addGridField(
        "Frais de trajet",
        `${allData.travelPrice.toLocaleString()} FCFA`,
        margin,
        y,
      );
      y += 12;

      pdf.setFont("helvetica", "bold");
      const paymentStatusText =
        selectedMethod === "recipient"
          ? "Total à payer par le Destinataire"
          : "Total payé par l'Expéditeur";
      addGridField(
        paymentStatusText,
        `${totalPrice.toLocaleString()} FCFA`,
        margin,
        y,
      );

      y += 20;

      // --- 5. SIGNATURES ---
      addSectionTitle("Signature");
      const sigY = y;

      // Client Header
      pdf.setFont("helvetica", "bold");
      pdf.text("Client", margin + 20, sigY);
      pdf.line(margin + 20, sigY + 1, margin + 31, sigY + 1); // Small underline

      // Agent Header
      pdf.text("Agent", pageWidth - margin - 35, sigY);
      pdf.line(
        pageWidth - margin - 35,
        sigY + 1,
        pageWidth - margin - 23,
        sigY + 1,
      );

      // Render actual client signature if exists
      if (allData.signatureUrl) {
        try {
          pdf.addImage(
            allData.signatureUrl,
            "PNG",
            margin + 10,
            sigY + 5,
            40,
            20,
          );
        } catch (e) {}
      }

      y += 45;

      // --- 6. FOOTER ---
      const finalY = pdf.internal.pageSize.getHeight() - 10;
      pdf.setFontSize(7);
      pdf.setTextColor(150, 150, 150);
      pdf.setDrawColor(200, 200, 200);
      pdf.line(margin, finalY - 5, pageWidth - margin, finalY - 5);

      const timestamp = new Date().toLocaleString("fr-FR");
      pdf.text(`Document généré le ${timestamp}.`, pageWidth / 2, finalY - 2, {
        align: "center",
      });
      pdf.text(
        `Ce document fait office de preuve de dépôt. ${APP_NAME} vous remercie.`,
        pageWidth / 2,
        finalY + 1,
        { align: "center" },
      );

      // Save
      pdf.save(`Bordereau_${trackingNumber}.pdf`);
    } catch (error) {
      console.error("Erreur génération PDF:", error);
    }
  },
};
