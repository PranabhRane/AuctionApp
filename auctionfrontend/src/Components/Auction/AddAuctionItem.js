import React, { useState } from "react";
import api from "../API";
import { useNavigate } from "react-router-dom";

const AddItem = () => {
  const navigate = useNavigate();
  const [formErrors, setFormErrors] = useState(false);

  const [auctionItem, setAuctionItem] = useState({
    itemName: "",
    reservedPrice: "",
    startTime: "",
    endTime: "",
  });

  const changeHandler = (e) => {
    const { name, value } = e.target;
    setAuctionItem((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const submitHandler = (e) => {
    e.preventDefault();
    const error = {};
    if (!auctionItem.itemName) {
      error.itemName = "Please enter Item Name";
    }
    if (!auctionItem.reservedPrice) {
      error.reservedPrice = "Please enter Reserved Price";
    }
    if (!auctionItem.startTime) {
      error.startTime = "Please enter Start Time";
    }
    if (!auctionItem.endTime) {
      error.endTime = "Please enter End Time";
    }
    if (Object.keys(error).length === 0) {
      saveAuctionItem();
    } else {
      setFormErrors(error);
    }
  };

  const saveAuctionItem = () => {
    api
      .post("/auction", auctionItem)
      .then((res) => {
        if (res.status === 200 && res.data) {
          navigate("/auction/list", { replace: true });
        }
      })
      .catch((error) => {
        if (error.response.status === 403) {
          navigate("/login");
        } else {
          alert(error.response.data);
        }
      });
  };

  return (
    <div className={`container`}>
      <form className="card-form">
        <div className="form-group">
          <h1>Add Auction Item</h1>
          <input
            type="text"
            name="itemName"
            id="itemName"
            className="form-control  input-sm"
            placeholder="Enter item name to be auctioned"
            onChange={changeHandler}
            value={auctionItem.itemName}
          />
          <p className="text-danger">{formErrors.itemName}</p>
        </div>
        <br />
        <div className="form-group">
          <input
            type="number"
            name="reservedPrice"
            id="reservedPrice"
            className="form-control"
            placeholder="Enter Reserved Price"
            onChange={changeHandler}
            value={auctionItem.reservedPrice}
          />
          <p className="text-danger">{formErrors.reservedPrice}</p>
        </div>
        <br />

        <div className="form-group">
          <input
            type="datetime-local"
            id="starttime"
            value={auctionItem.startTime}
            name="startTime"
            className="form-control"
            onChange={changeHandler}
          />
          <p className="text-danger">{formErrors.startTime}</p>
        </div>
        <br />

        <div className="form-group">
          <input
            type="datetime-local"
            id="endtime"
            className="form-control"
            value={auctionItem.endTime}
            name="endTime"
            onChange={changeHandler}
          />
          <p className="text-danger">{formErrors.endTime}</p>
        </div>
        <br />
        <div className="row justify-content-center">
          <button
            className="col-md-4 btn btn-primary btn-lg"
            onClick={submitHandler}
          >
            Submit
          </button>
        </div>

        <br />
        <div className="row justify-content-center">
          <button
            className="col-md-4 btn btn-primary btn-lg"
            onClick={() => navigate("/auction/list")}
          >
            Auction List
          </button>
        </div>
      </form>
    </div>
  );
};
export default AddItem;
