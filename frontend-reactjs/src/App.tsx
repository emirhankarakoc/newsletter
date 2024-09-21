import { Route, Routes } from "react-router-dom";
import Register from "./pages/register/Register";
import Login from "./pages/login/Login";
import Profile from "./pages/profile/Profile";
import Page from "./pages/landing/Page";
import Dashboard from "./pages/dashboard/Dashboard";
import { CreateNewsletter } from "./pages/createnewsletters/CreateNewsletter";
import { JoinAsSubscriber } from "./pages/joinsubs/JoinAsSubscriber";
function App() {
  return (
    <Routes>
      <Route path="/" element={<Page />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/profile" element={<Profile />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route
        path="/dashboard/newsletters/create"
        element={<CreateNewsletter />}
      />

      <Route path="/join/:newsletterId" element={<JoinAsSubscriber />} />
    </Routes>
  );
}

export default App;
