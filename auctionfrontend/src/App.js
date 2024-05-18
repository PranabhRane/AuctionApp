import "./App.css";
import AddAuctionItem from "./Components/Auction/AddAuctionItem";
import Login from "./Components/Login/Login";
import Register from "./Components/Register/Register";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Bidding from "./Components/Bidding/Bidding";
import AuctionList from "./Components/Auction/AutionList";
import Report from "./Components/Reports/Report";
import { PrivateRoute } from "./Components/PrivateRoute";
import BiddingList from "./Components/Bidding/BidddingList";

function App() {

  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/auction/create" element={<PrivateRoute component={AddAuctionItem} alowedRoles={["AUCTIONEER"]}></PrivateRoute>}></Route>
          <Route path="/bid/:id" element={<PrivateRoute component={Bidding} alowedRoles={["PARTICIPANT"]}></PrivateRoute>}></Route>
          <Route path="/auction/bidlist" element={<PrivateRoute component={BiddingList} alowedRoles={["PARTICIPANT"]}></PrivateRoute>}></Route>
          <Route path="/report" element={<PrivateRoute component={Report} alowedRoles={["ADMIN"]}></PrivateRoute>}></Route>
          <Route path="/auction/list" element={<PrivateRoute component={AuctionList} alowedRoles={["AUCTIONEER"]}></PrivateRoute>}></Route>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/signup" element={<Register />}></Route>
        </Routes>
      </Router>
    </div>
  );
}

export default App;
