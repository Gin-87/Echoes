import React from "react";
import Carousel from "react-material-ui-carousel";
import { Box } from "@mui/material";

function CarouselComponent({ items }) {
  return (
      <Box sx={{ position: "relative" }}>
        <Carousel
            autoPlay
            animation="slide"
            indicators={true}
            indicatorContainerProps={{
              style: {
                zIndex: 10,
              },
            }}
        >
          {items.map((item, index) => (
              <Box
                  key={index}
                  component="img"
                  src={item.image}
                  alt={item.alt}
                  sx={{
                    width: "100%",
                    height: "450px",
                    objectFit: "cover",
                    borderRadius: "8px",
                  }}
              />
          ))}
        </Carousel>
      </Box>
  );
}


export default CarouselComponent;