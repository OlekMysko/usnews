import React from 'react';

const NewsItem = ({ title, content, onNewsClick, isSelected }) => {
    return (
        <li
            onClick={() => onNewsClick(content)}
            style={{
                cursor: 'pointer',
                marginBottom: '10px',
                padding: '10px',
                backgroundColor: isSelected ? '#e0e0e0' : '#f8f8f8',
                border: '1px solid #ddd',
                borderRadius: '8px',
                transition: 'background-color 0.3s',
            }}
            onMouseEnter={(e) => !isSelected && (e.currentTarget.style.backgroundColor = '#e0e0e0')}
            onMouseLeave={(e) => !isSelected && (e.currentTarget.style.backgroundColor = '#f8f8f8')}
        >
            {title}
        </li>
    );
};

export default NewsItem;
