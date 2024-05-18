import React, { useEffect, useState } from "react";
import api from "../API";
import { useNavigate } from "react-router-dom";
import { Table } from "../Table/Table";
import { format } from "date-fns";

const AuctionList = () => {
  const [auctionList, setAuctionList] = useState([]);

  const navigate = useNavigate();

  const COLUMNS = [
    {
      Header: "Id",
      Footer: "Id",
      accessor: "id",
      disableFilters: true,
      sticky: "left",
    },
    {
      Header: "Item Name",
      Footer: "Item Name",
      accessor: "itemName",
      sticky: "left",
    },
    {
      Header: "Reserved price",
      Footer: "Reserved price",
      accessor: "reservedPrice",
      sticky: "left",
    },
    {
      Header: "Start Time",
      Footer: "Start Time",
      accessor: "startTime",
      sticky: "left",
      Cell: ({ value }) => {
        return value ? format(value, "yyyy-MM-dd HH:mm:ss") : "";
      },
    },
    {
      Header: "End Time",
      Footer: "End Time",
      accessor: "endTime",
      sticky: "left",
      Cell: ({ value }) => {
        return value ? format(value, "yyyy-MM-dd HH:mm:ss") : "";
      },
    },
    {
      Header: "Last Bid",
      Footer: "Last Bid",
      accessor: "lastBid",
      sticky: "left",
    },
    {
      Header: "Status",
      Footer: "Status",
      accessor: "status",
      sticky: "left",
    },
  ];

  useEffect(() => {
    setData();
  }, []);

  const setData = async () => {
    api
      .get("/auction")
      .then((response) => {
        setAuctionList(response.data);
      })
      .catch((error) => {
        if (error.response.status === 403) {
          navigate("/login");
        }
      });
  };

  return (
    <>
      <div className="container">
        <div className="display-flex">
          <h1 className="ml-auto">Auction Details List</h1>
          <button
            style={{ float: "right" }}
            onClick={() => navigate("/auction/create")}
            className="btn btn-primary btn-sm"
          >
            Add Auction Item
          </button>
        </div>
        <Table data={auctionList} columns={COLUMNS} />
      </div>
    </>
  );
};

export default AuctionList;
