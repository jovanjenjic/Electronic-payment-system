import React from 'react';
import styled from 'styled-components';
import { Popover, Badge } from 'antd';
import { useHistory } from 'react-router-dom';

import { ShoppingCartOutlined, CreditCardOutlined } from '@ant-design/icons';

import ItemList from './ItemList';
import { post } from '../services/api';
import { responseOk } from '../utils/responseOk';

import { ShopContext } from '../context/shop';
import {
  BTC_CREATE_PAYMENT_URL,
  PAYPAL_PAYMENT_URL,
  BANK_CREATE_PAYMENT_URL,
} from '../constants/url';

const Container = styled.div`
  &:hover {
    cursor: pointer;
  }
  margin-right: 10px;
`;

const PaymentGroup = styled.div`
  border-top: 1px solid #f1f3f5;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  padding-top: 10px;
`;

const imgStyle = {
  width: 24,
  height: 24,
};

const ImgContainer = styled.div`
  &:hover {
    cursor: pointer;
  }
`;

const Title = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;
`;

const createPaymentPaypal = async ({
  price,
  count,
  currency = 'EUR',
  id,
}) => {
  const authToken = localStorage.getItem('access_token');

  const response = await post(
    PAYPAL_PAYMENT_URL,
    {
      price,
      itemsCount: count,
      itemId: id,
      currency,
      paymentType: 'PAYPAL'
    },
    authToken
  );

  if (responseOk(response)) {
    const { paymentUrl = '' } = await response.json();
    // open payment url
    window.open(paymentUrl);
    return { error: false };
  }

  return { error: true };
};

const createPaymentBtc = async ({
  price,
  count,
  currency = 'EUR',
  id,
}) => {
  const authToken = localStorage.getItem('access_token');

  const response = await post(
    BTC_CREATE_PAYMENT_URL,
    {
      price,
      itemsCount: count,
      itemId: id,
      currency,
      paymentType: 'BTC'
    },
    authToken
  );

  if (responseOk(response)) {
    const { payment_url: paymentUrl = '' } = await response.json();
    // open payment url
    window.open(paymentUrl);
    return { error: false };
  }

  return { error: true };
};

// TODO: update bank also later

const createPaymentBank = async ({ price, count, id, currency = 'EUR' }, history) => {
  const authToken = localStorage.getItem('access_token');

  try {
    const response = await post(
      BANK_CREATE_PAYMENT_URL,
      {
        price,
        itemsCount: count,
        itemId: id,
        currency,
        paymentType: 'BANK'
      },
      authToken
    );

    if (responseOk(response)) {
      const result = await response.json();
      history.push('/bank/payment', result);
      return { error: false };
    }
  } catch (error) {
    console.error(error);
  }

  return { error: true };
};

const ShoppingCart = () => {
  const history = useHistory();

  const { items = [], paymentTypes, updateItems, user = {} } = React.useContext(
    ShopContext
  );

  const [item = { count: 0 }] = items;

  /** `discount` for the memberships */
  const maxDiscount = user?.subscriptionList?.length ? Math.max(...user.subscriptionList.map(v => v.discount)) : 0;

  // TODO: Send request for payments here payments here
  /** `types` name */
  const types = paymentTypes.map((v) => v.type);

  const handlePaypal = async () => {
    const { error } = await createPaymentPaypal({ ...item, price: (item.price * (1 - maxDiscount / 100)).toFixed(2) });
    if (!error) {
      updateItems(() => []);
    }
  };

  const handleBtc = async () => {
    const { error } = await createPaymentBtc({ ...item, price: (item.price * (1 - maxDiscount / 100)).toFixed(2) });
    if (!error) {
      updateItems(() => []);
    }
  };

  const handleBank = async () => {
    const { error } = await createPaymentBank({ ...item, price: (item.price * (1 - maxDiscount / 100)).toFixed(2) }, history);
    if (!error) {
      updateItems(() => []);
    }
  };

  return (
    <Container>
      <Popover
        title={
          <Title>
            <ShoppingCartOutlined />
            {`Cart`}
          </Title>
        }
        content={
          <div>
            <ItemList items={items} user={user} />
            <PaymentGroup>
              {/* {types.includes(PAYPAL) && ( */}
              <ImgContainer onClick={handlePaypal}>
                <img src="../img/paypal.png" style={imgStyle} />
              </ImgContainer>
              {/* )} */}
              {/* {types.includes(BITCOIN) && ( */}
              <ImgContainer onClick={handleBtc}>
                <img src="../img/btc.png" style={imgStyle} />
              </ImgContainer>
              {/* )} */}
              {/* {types.includes(BANK) && ( */}
              <ImgContainer onClick={handleBank}>
                <CreditCardOutlined />
              </ImgContainer>
              {/* )} */}
            </PaymentGroup>
          </div>
        }
        placement="bottomRight"
        trigger="click"
      >
        <Badge count={item.count} offset={[0, -2]}>
          <ShoppingCartOutlined style={{ fontSize: 16, marginRight: 10 }} />
        </Badge>
      </Popover>
    </Container>
  );
};

export default ShoppingCart;
