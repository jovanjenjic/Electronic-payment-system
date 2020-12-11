import React from 'react';
import styled from 'styled-components';
import { Popover, Badge } from 'antd';

import {
  ShoppingCartOutlined,
  CreditCardOutlined,
} from '@ant-design/icons';

import ItemList from './ItemList';

import { ShopContext } from '../context/shop';
import { PAYPAL, BITCOIN, BANK } from '../constants/paymentTypes';

const Container = styled.div`  
  &:hover {
    cursor: pointer;
  }
  margin-right: 10px;
`;

const PaymentGroup = styled.div`
  border-top: 1px solid #F1F3F5;
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

const ShoppingCart = () => {

  const { items = [], paymentTypes } = React.useContext(ShopContext);

  const [item = { count: 0 }] = items;

  // TODO: Send request for payments here payments here
  /** `types` name */
  const types = paymentTypes.map(v => v.type);

  return (
    <Container>
      <Popover
        title={<Title><ShoppingCart />{`Cart`}</Title>}
        content={
          <div>
            <ItemList items={items} />
            <PaymentGroup>
              {types.includes(PAYPAL) &&
                <ImgContainer>
                  <img src="../img/paypal.png" style={imgStyle} />
                </ImgContainer>
              }
              {types.includes(BITCOIN) &&
                <ImgContainer>
                  <img src="../img/btc.png" style={imgStyle} />
                </ImgContainer>
              }
              {types.includes(BANK) &&
                <ImgContainer>
                  <CreditCardOutlined />
                </ImgContainer>
              }
            </PaymentGroup>
          </div>
        }
        placement="bottomRight"
        trigger="click"
      >
        <Badge count={item.count} offset={[0, -2]}><ShoppingCartOutlined style={{ fontSize: 16, marginRight: 10 }} /></Badge>
      </Popover>
    </Container>
  );
};

export default ShoppingCart;