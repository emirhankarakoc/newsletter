import React, { useState } from "react";
import Footer from "@/components/Footer";
import Navigation from "@/components/Navigation";
export const CreateMail = () => {
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
</html>
`
  );

  const handleDownload = () => {
    const blob = new Blob([htmlContent], { type: "text/html" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    const now = new Date().toLocaleString().replace(/[/ :]/, "-"); // '/' ve ':' karakterlerini '-' ile değiştir
    a.download = "Gonderilecek_Mail_Ornegi" + now + ".html"; // İndirilecek dosya adı
    a.href = url;

    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
  };

  return (
    <div>
      <Navigation />
      <div className="min-h-[650px] p-5">
        <textarea
          value={htmlContent}
          onChange={(e) => setHtmlContent(e.target.value)}
          className="w-full h-64 border p-2"
          placeholder="HTML içeriğinizi buraya yazın..."
        />
        <div className="mt-4">
          <h2>Önizleme:</h2>
          <div
            className="border p-2"
            dangerouslySetInnerHTML={{ __html: htmlContent }}
          />
        </div>
        <button
          onClick={handleDownload}
          className="mt-4 bg-blue-500 text-white p-2"
        >
          Generate
        </button>
      </div>
      <Footer />
    </div>
  );
};
