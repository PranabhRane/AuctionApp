import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../API";

const Bidding = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [amount, setAmount] = useState();
  const [formError, setFormError] = useState(false);
  const [currentAuction, setCurrentAuction] = useState({
    itemName: "",
    lastBid: "",
  });

  useEffect(() => {
    api.get(`/auction/${id}`).then((res) => {
      setCurrentAuction(res.data);
    });
  }, [id]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!amount) {
      setFormError(true);
    }
    api
      .post(`/bids/${id}/${amount}`)
      .then((res) => {
        navigate("/auction/bidlist");
      })
      .catch((error) => {
        if (error.response.status === 403) {
          navigate("/login");
        }else{
          alert(error.response.data);
        }
      });
  };

  return (
    <div className="container">
      <form className="card-form">
        <h1>Bidding Form</h1>
        <div className="form-group">
          <label htmlFor="name">Item Name :</label>
          <input
            type="name"
            name="name"
            id="name"
            className="form-control"
            value={currentAuction.itemName}
            readOnly={true}
          />
        </div>

        <div className="form-group">
          <label htmlFor="lastbid">Last Bid :</label>
          <input
            type="input"
            name="lastbid"
            id="lastbid"
            className="form-control"
            value={currentAuction.lastBid}
            style={{ outline: "none", border: "none" }}
            readOnly={true}
          />
        </div>

        <div className="form-group">
          <label htmlFor="price">Bid Price :</label>
          <input
            type="number"
            name="price"
            id="price"
            className="form-control"
            placeholder="Enter Bid Price"
            onChange={(e) => {
              setAmount(e.target.value);
            }}
            value={amount}
          />
          <p className="text-danger">{formError}</p>
        </div>

        <br />
        <div className="row">
          <button className="btn btn-primary btn-lg" onClick={handleSubmit}>
            Place Bid
          </button>
        </div>
        <br />
        <div className="row">
          <button
            className="btn btn-primary btn-lg"
            onClick={() => {
              navigate("/auction/bidlist");
            }}
          >
            Bid List
          </button>
        </div>
      </form>
    </div>
  );
};
export default Bidding;
