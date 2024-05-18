import React, { useEffect, useState } from "react";
import api from "../API";
import { Table } from "../Table/Table";
import { useNavigate } from "react-router-dom";

const BiddingList = () => {
  const [biddingList, setBiddingList] = useState([]);
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
      Header: "Last Bid",
      Footer: "Last Bid",
      accessor: "lastBid",
      sticky: "left",
    },
    {
      Header: "Action",
      Footer: "Action",
      accessor: "action",
      Cell: ({ row }) => {
        return (
          <button
            className="btn btn-primary"
            onClick={() => navigate(`/bid/${row.original.id}`)}
          >
            Place Bid
          </button>
        );
      },
    },
  ];

  useEffect(() => {
    setData();
  }, []);

  const setData = async () => {
    api
      .get("/auction/active")
      .then((response) => {
        setBiddingList(response.data);
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
        <div className="row">
          <h1>Active Auctions</h1>
        </div>
        <div className="row">
          <Table data={biddingList} columns={COLUMNS} />
        </div>
      </div>
    </>
  );
};

export default BiddingList;
