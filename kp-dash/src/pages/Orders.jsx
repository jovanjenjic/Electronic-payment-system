import React from 'react';
import { Table, Tag } from 'antd';
import styled from 'styled-components';
import { CreditCardOutlined } from '@ant-design/icons';

import usefetchOrders from '../hooks/useFetchOrders';


const Container = styled.div`
  display: flex;
  padding: 25px;
`;

const ImgContainer = styled.div`
  &:hover {
    cursor: pointer;
  }
`;

const imgStyle = {
  width: 24,
  height: 24,
};

const columns = [
  {
    title: 'Id',
    dataIndex: 'id',
    key: 'id'
  },
  {
    title: 'Price',
    dataIndex: 'price',
    key: 'price'
  },
  {
    title: 'Number of items',
    dataIndex: 'itemsCount',
    key: 'itemsCount'
  },
  {
    title: 'Total',
    dataIndex: 'total',
    key: 'total',
    render: (_, record) => record.price * record.itemsCount,
  },
  {
    title: 'Currency',
    dataIndex: 'currency',
    key: 'currency'
  },
  {
    title: 'Description',
    dataIndex: 'description',
    key: 'description'
  },
  {
    title: 'Date',
    dataIndex: 'createdAt',
    key: 'createdAt',
    // render: (value) => new Date(value).toLocaleDateString(),
  },
  {
    title: 'Payment type',
    dataIndex: 'paymentType',
    key: 'paymentType',
    render: (value) => {
      if (value === 'PAYPAL') return (
        <ImgContainer>
          <img src="../img/paypal.png" style={imgStyle} />
        </ImgContainer>
      );
      if (value === 'BTC') return (
        <ImgContainer>
          <img src="../img/btc.png" style={imgStyle} />
        </ImgContainer>
      );
      if (value === 'BANK') return (
        <ImgContainer>
          <CreditCardOutlined />
        </ImgContainer>
      );
      return value;
    }
  },
  {
    title: 'State',
    dataIndex: 'orderState',
    key: 'orderState',
    // eslint-disable-next-line react/display-name
    render: (value) => {
      let color = 'green';
      if (value === 'CREATED') color = 'blue';
      else if (value === 'CANCELED') color = 'gold';
      else if (value === 'FAILED') color = 'red';
      return (
        <Tag color={color}>
          {value}
        </Tag>
      );
    }
  }
];


const Orders = () => {

  const { data = [] } = usefetchOrders();

  return (
    <Container>
      <Table dataSource={data.sort((a, b) => b.id - a.id)} rowKey="id" columns={columns} />
    </Container>
  );
};

export default Orders;