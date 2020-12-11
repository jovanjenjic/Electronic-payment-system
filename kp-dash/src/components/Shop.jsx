import React from 'react';
import styled from 'styled-components';

import { Card } from 'antd';
import { ShoppingCartOutlined, SendOutlined } from '@ant-design/icons';

import { ShopContext } from '../context/shop';

const { Meta } = Card;

const Container = styled.div`
  display: grid;
  grid-template-columns: 25% 25% 25% 25% 25%;
  padding: 20px;

  @media only screen and (max-width: 600px) {
    display: grid;
    grid-template-columns: 50% 50%;
  }
`;

const CardWrapper = styled.div`
  margin: 10px;
  transition: all .2s ease-in-out;

  &:hover {
    cursor: pointer;
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
    transform: scale(1.05);
  }
`;

const Description = styled.div`
  display: flex;
  flex-direction: column;

  p {
    margin-bottom: 0px;
  }

  &.price {
    font-size: 13px;
    font-family: 'Arimo';
    font-weight: 700;
  }
`;

const dummyData = [
  {
    id: 1,
    price: 3,
    description: 'Nice boook',
    title: 'Konan',
    img: '../img/book.jpg',
  },
  {
    id: 2,
    price: 5,
    description: 'Boom',
    title: 'Varvarin',
    img: '../img/book.jpg',
  },
];


const Shop = () => {

  const { updateItems } = React.useContext(ShopContext);

  const handleClick = (id) => {
    updateItems(prevItems => {
      const foundItem = prevItems.find(v => v.id === id);
      return foundItem ? [{ ...foundItem, count: (foundItem.count || 0) + 1 }] : [{ ...dummyData.find(v => v.id === id), count: 1 }];
    });
  };

  // TODO: Add subscribe option here

  return (
    <Container>
      {dummyData.map(({ id, title, description, img, price }) => {

        return (
          <CardWrapper key={id}>
            <Card
              cover={
                <img
                  alt={`${id}-example`}
                  src={img}
                  style={{ height: 150 }}
                />
              }
              actions={[
                <div key="Shop" onClick={() => handleClick(id)} ><ShoppingCartOutlined key="Shop" style={{ marginRight: 10 }} /><span>Buy</span></div>,
                <div key="Subscribe"> <SendOutlined key="Subscribe" style={{ marginRight: 10 }} />Subscribe</div>
              ]}
            >
              <Meta
                title={title}
                description={
                  <Description>
                    <p>{description}</p>
                    <div className="price">{`Price: ${price} EUR`}</div>
                  </Description>
                }
              />
            </Card>
          </CardWrapper>
        );
      })}
    </Container>
  );
};

export default Shop;