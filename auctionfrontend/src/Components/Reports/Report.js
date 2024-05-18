import React, { useState } from "react";
import api from "../API";

const Report = () => {
  var date = new Date();
  date.setDate(date.getDate() - 1);

  var today = date.toISOString().split("T")[0];

  const [reportDate, setReportDate] = useState(new Date());

  const submitHandler = async (e) => {
    e.preventDefault();
    try {
      const response = await api.get(`/report/generate/${reportDate}`, {
        responseType: "blob",
      });
      //const blob = await response.arrayBuffer();
      const url = window.URL.createObjectURL(response.data);
      const a = document.createElement("a");
      a.href = url;
      a.download = "generated_file.xlsx"; // Specify the filename here
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error downloading file:", error);
    }
  };

  return (
    <div className="container">
      <form className="card-form" style={{ width: "40%", marginLeft: "350px" }}>
        <h1>Reporting</h1>
        <div className="form-group">
          <label htmlFor="date">Select Date :</label>
          <input
            type="date"
            className="form-control"
            name="date"
            max={today}
            onChange={(e) => setReportDate(e.target.value)}
          />
        </div>
        <br />
        <button className="btn btn-primary " onClick={submitHandler}>
          Submit
        </button>
      </form>
    </div>
  );
};
export default Report;
