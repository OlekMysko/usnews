import React from 'react';
import NewsItem from './NewsItem';

const NewsList = ({ news, onNewsClick, selectedNewsContent }) => {
    return (
        <ul style={{ listStyleType: 'none', padding: 0 }}>
            {news.map((item, index) => (
                <NewsItem
                    key={index}
                    title={item.title}
                    content={item.content}
                    onNewsClick={onNewsClick}
                    isSelected={selectedNewsContent === item.content}
                />
            ))}
        </ul>
    );
};

export default NewsList;
