import { APIURL, CLIENTURL, http, httpError } from "@/assets/http";
import { useEffect, useState } from "react";
import { useGoogleLogin } from "@react-oauth/google";
import Navigation from "@/components/Navigation"; // Eğer Navigation bileşenini doğru çağırıyorsanız
import { Button } from "@nextui-org/button";
import Footer from "@/components/Footer";
import axios from "axios";
import { Accordion, AccordionItem } from "@nextui-org/react";
import { toast, Toaster } from "sonner";

export default function Dashboard() {
  const [oauth2Response, setOauth2Response] = useState<OAuth2Account>();
  const [user, setUser] = useState<User>(); // loggedUser olarak state kullanıyorsunuz
  const [isLoading, setLoading] = useState<boolean>(false);
  const [newsletters, setNewsletters] = useState<Newsletter[]>();

  useEffect(() => {
    const handleGetMe = async () => {
      try {
        const response = await http.get("/accounts/getme");
        setUser(response.data); // loggedUser state'ini güncelleyip setUser() kullanıyorsunuz
        console.log(response.data);
      } catch (err) {
        httpError(err);
      }
    };
    const handleGetNewsletters = async () => {
      try {
        const response = await http.get("/newsletters/my");
        setNewsletters(response.data);
      } catch (err) {
        httpError(err);
      }
    };

    handleGetNewsletters();
    handleGetMe();
  }, []);

  const login = useGoogleLogin({
    onSuccess: (codeResponse: any) => {
      setOauth2Response(codeResponse);
      //   window.location.href = "/dashboard";
    },
    onError: (error) => console.log("Login Failed:", error),
    scope:
      "openid profile email https://www.googleapis.com/auth/gmail.readonly https://www.googleapis.com/auth/gmail.send",
  });
  useEffect(() => {
    const sendDatasToBackend = async () => {
      try {
        const response = await axios.get(
          `https://www.googleapis.com/oauth2/v1/userinfo?access_token=${oauth2Response!.access_token}`,
          {
            headers: {
              Authorization: `Bearer ${oauth2Response!.access_token}`,
              Accept: "application/json",
            },
          }
        );
        console.log("poslanan data" + oauth2Response);

        await http.post(
          `/accounts/oauth2/${oauth2Response?.access_token}`,
          response.data
        );
        window.location.href = "/dashboard";
      } catch (error) {
        httpError(error);
      }
    };
    sendDatasToBackend();
  }, [oauth2Response]);

  const handleLogOutOAuth2Account = async () => {
    setLoading(true);
    try {
      await http.post(`/accounts/delete/oauth2connection/${user?.id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("jwtToken")}`,
          "Content-Type": "application/json",
        },
      });
      window.location.href = "/dashboard";
    } catch (errr) {
      httpError(errr);
    }
    setLoading(false);
  };

  return (
    <div>
      <Navigation />
      <div className="grid grid-cols-4 p-20 gap-5">
        <div className="col-span-1 ">
          <div className="border-3 border-pink-400 p-5 grid place-items-center gap-3">
            <h1 className="text-xl font-bold font-sfpro">Account</h1>
            <img src="https://picsum.photos/100" alt="user image" />
            <p>e-mail address:{user?.email}</p>
            <p className="font-bold">status:{user?.userStatus}</p>
          </div>
          {user?.userStatus === "GOOGLE_NOT_VERIFICATED" && (
            <div className="my-3 bg-blue-500 grid place-items-center rounded-3xl p-10 hover:bg-blue-600 hover:text-white">
              <button onClick={() => login()}>
                Connect account with Google 🚀
              </button>
            </div>
          )}
          {user?.userStatus === "GOOGLE_VERIFICATED" && (
            <div className="border-3 border-pink-400 p-5 grid place-items-center gap-3">
              <h1 className="text-xl font-bold font-sfpro">
                Mail Sender Account
              </h1>
              <img src={user?.oauth2.picture} alt="user image" />
              <p>{user?.oauth2.name}</p>
              <p>{user?.oauth2.email}</p>
              <div>
                <Button color="danger" onClick={handleLogOutOAuth2Account}>
                  log out from google account
                </Button>
              </div>
            </div>
          )}
        </div>
        {user?.userStatus === "GOOGLE_VERIFICATED" ? (
          <div className="col-span-3 border-3 border-pink-400 p-5">
            <h1 className="text-3xl font-bold font-sfpro">Dashboard</h1>
            <Accordion defaultExpandedKeys={["1"]}>
              <AccordionItem
                key="1"
                aria-label="Newsletters"
                title="Newsletters"
              >
                <a href="/dashboard/newsletters/create">
                  <Button fullWidth color="success">
                    Create new one!
                  </Button>
                </a>
                {newsletters?.map((newsletter, index) => (
                  <div
                    key={index}
                    className="grid grid-cols-12 my-3 bg-gray-200 rounded-3xl p-5"
                  >
                    <img
                      className="col-span-2 w-20 h-20"
                      src={newsletter.imageUrl}
                      alt="newsletter thumbnail"
                    />
                    <div className="col-span-2 grid place-items-center font-bold font-sfpro">
                      {newsletter.name}
                    </div>
                    <div className="col-span-2 grid place-items-center font-bold font-sfpro">
                      {newsletter.customers.length} members
                    </div>
                    <div className="col-span-6 flex flex-row items-center justify-center gap-10">
                      <a href="#">
                        <Button color="success" title="Send Mail">
                          <i className="fa-solid fa-plus"></i>
                        </Button>
                      </a>

                      <Button
                        color="secondary"
                        title="Copy sharing link"
                        onClick={() => {
                          const shareUrl = `${CLIENTURL}/join/${newsletter.id}`;
                          navigator.clipboard
                            .writeText(shareUrl)
                            .then(() => {
                              console.log(
                                "URL copied to clipboard: ",
                                shareUrl
                              );
                              toast.success("URL copied to clipboard.");
                            })
                            .catch((err) => {
                              console.error("Failed to copy the URL: ", err);
                            });
                        }}
                      >
                        <i className="fa-solid fa-share-nodes"></i>
                      </Button>

                      <Button
                        color="primary"
                        title="Subscribers"
                        onClick={() => {
                          window.location.href = `/dashboard/newsletters/${newsletter.id}/subscribers`;
                          console.log("");
                        }}
                      >
                        <i className="fa-solid fa-user"></i>
                      </Button>
                      <Button
                        color="warning"
                        title="Update"
                        onClick={() => {
                          window.location.href = "/update/" + newsletter.id;
                        }}
                      >
                        <i className="fa-solid fa-pen-to-square"></i>
                      </Button>
                      <Button color="danger" title="Don't click">
                        <i className="fa-solid fa-trash-can"></i>
                      </Button>
                    </div>
                  </div>
                ))}
              </AccordionItem>
              <AccordionItem
                key="2"
                aria-label="burayi degistir"
                title="burayi degistir"
              >
                <p>araba</p>
              </AccordionItem>
            </Accordion>
          </div>
        ) : (
          <div className="border-pink-300 border-5 grid place-items-center p-5">
            <h1 className="font-bold font-sfpro text-3xl ">
              Connect gmail account for use this website.
            </h1>
          </div>
        )}
      </div>

      <Footer />
    </div>
  );
}
