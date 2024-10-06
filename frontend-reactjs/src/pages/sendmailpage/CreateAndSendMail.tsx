import React, { useState } from "react";
import Footer from "@/components/Footer";
import Navigation from "@/components/Navigation";
import { Button } from "@nextui-org/react";
import { http } from "@/assets/http";
import { useParams } from "react-router-dom";

export const CreateAndSendMail = () => {
  const { newsletterId } = useParams();
  const [htmlContent, setHtmlContent] = useState(
    `<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Document</title>
  </head>
  <body>
    <table width="100%" cellpadding="0" cellspacing="0" border="0">
      <tr>
        <td style="text-align: center">
          <h1 style="margin: 0">
            e-Newsletter Service, easy way to start blogging!
          </h1>
          <p style="margin: 0">degerli vayne severler dernegi. hosgeldiniz!</p>
          <table style="margin: 0 auto">
            <tr>
              <td style="border: 5px solid lightgreen; padding: 10px">
                <img
                  src="http://res.cloudinary.com/dhoj5fmxr/image/upload/v1727015216/cmah2y1cigd1kmaqjpb5.jpg"
                  alt="200x200 resim"
                  style="display: block"
                />
              </td>
            </tr>
          </table>
          <p style="margin: 0">
            burasi da footer yeri, istege bagli unsub mesajini barindirabilir. koysaniz iyi de olur. 
          </p>
        </td>
      </tr>
    </table>
  </body>
</html>`
  );
  const [subject, setSubject] = useState(""); // Subject state
  const [isLoading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault(); // Prevent default form behavior
    setLoading(true);

    // Convert HTML content to a Blob file
    const htmlBlob = new Blob([htmlContent], { type: "text/html" });
    const formData = new FormData();

    // Append the subject and HTML file to FormData
    formData.append("subject", subject);
    formData.append("htmlFile", htmlBlob, "newsletter.html"); // Set the file name

    try {
      const response = await http.post(
        `/newsletters/${newsletterId}/sendMessage`,
        formData,
        { headers: { "Content-Type": "multipart/form-data" } } // Set the correct headers for FormData
      );

      console.log("Mail successfully sent to subscribers:", response.data);
    } catch (error) {
      console.error("Error sending mail:", error);
    }

    setLoading(false);
  };

  return (
    <div>
      <Navigation />
      <p className="text-center p-5 border-pink-300 border-3">
        Welcome. You need to use external HTML code editor for test this code.
      </p>
      <form onSubmit={handleSubmit} className="min-h-[650px] p-5">
        {/* Subject Input Field */}
        <div className="mb-4">
          <label htmlFor="subject" className="block mb-2">
            Write here subject
          </label>
          <input
            type="text"
            id="subject"
            value={subject}
            onChange={(e) => setSubject(e.target.value)}
            className="w-full border p-2"
            placeholder="Enter a subject..."
            required
          />
        </div>

        {/* HTML Content Area */}
        <div className="mb-4">
          <label htmlFor="htmlContent" className="block mb-2">
            HTML İçeriği:
          </label>
          <textarea
            id="htmlContent"
            value={htmlContent}
            onChange={(e) => setHtmlContent(e.target.value)}
            className="w-full h-64 border p-2"
            placeholder="HTML içeriğinizi buraya yazın..."
            required
          />
        </div>

        {/* Preview Area */}
        <div className="mt-4">
          <h2>Önizleme:</h2>
          <div
            className="border p-2"
            dangerouslySetInnerHTML={{ __html: htmlContent }}
          />
        </div>

        {/* Submit Button */}
        <Button
          isLoading={isLoading}
          type="submit"
          fullWidth
          color="success"
          className="mt-4 p-2"
        >
          Send to Subscribers!
        </Button>
      </form>
      <Footer />
    </div>
  );
};
