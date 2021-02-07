import React from 'react';
import styled from 'styled-components';

import { Card } from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';

import { ShopContext } from '../context/shop';

import useFetchItems from '../hooks/useFetchItems';

const { Meta } = Card;

const Container = styled.div`
  display: grid;
  grid-template-columns: 20% 20% 20% 20% 20%;
  padding: 20px;

  @media only screen and (max-width: 600px) {
    display: grid;
    grid-template-columns: 50% 50%;
  }
`;

const CardWrapper = styled.div`
  margin: 10px;
  transition: all 0.2s ease-in-out;

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

const Shop = () => {
  const { updateItems } = React.useContext(ShopContext);

  const { data: allItems = [] } = useFetchItems();

  const handleClick = (id) => {
    updateItems((prevItems) => {
      const foundItem = prevItems.find((v) => v.id === id);
      return foundItem
        ? [{ ...foundItem, count: (foundItem.count || 0) + 1 }]
        : [{ ...allItems.find((v) => v.id === id), count: 1 }];
    });
  };

  return (
    <Container>
      {allItems.map(({ id, title, description, img = '../img/book.jpg', price }) => {
        return (
          <CardWrapper key={id}>
            <Card
              cover={
                <img alt={`${id}-example`} src={img} style={{ height: 150 }} />
              }
              actions={[
                <div key="Shop" onClick={() => handleClick(id)}>
                  <ShoppingCartOutlined
                    key="Shop"
                    style={{ marginRight: 10 }}
                  />
                  <span>Buy</span>
                </div>
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
