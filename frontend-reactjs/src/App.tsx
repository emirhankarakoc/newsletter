import { Route, Routes } from "react-router-dom";
import Register from "./pages/register/Register";
import Login from "./pages/login/Login";
import Profile from "./pages/profile/Profile";
import Page from "./pages/landing/Page";
import Dashboard from "./pages/dashboard/Dashboard";
import { CreateNewsletter } from "./pages/createnewsletters/CreateNewsletter";
import { JoinAsSubscriber } from "./pages/joinsubs/JoinAsSubscriber";
import { Subscribers } from "./pages/subscribers/Subscribers";
import { UpdateNewsletterAndSubscribers } from "./pages/update-newsletter/UpdateNewsletterAndSubscribers";
import { CreateAndSendMail } from "./pages/sendmailpage/CreateAndSendMail";
function App() {
  return (
    <Routes>
      <Route path="/" element={<Page />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/profile" element={<Profile />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route
        path="/dashboard/newsletters/:newsletterId/subscribers"
        element={<Subscribers />}
      />

      <Route
        path="/dashboard/newsletters/create"
        element={<CreateNewsletter />}
      />

      <Route path="/join/:newsletterId" element={<JoinAsSubscriber />} />
      <Route
        path="/update/:newsletterId"
        element={<UpdateNewsletterAndSubscribers />}
      />
      <Route path="/sendmail/:newsletterId" element={<CreateAndSendMail />} />
    </Routes>
  );
}

export default App;
