CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    title TEXT,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP AT TIME ZONE 'UTC',
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    post_id INTEGER NOT NULL,
    parent_id INTEGER,
    user_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (parent_id) REFERENCES comments(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Adding indexes to improve retrieval speed
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_comments_parent_id ON comments(parent_id);


CREATE TYPE reaction_type AS ENUM ('like', 'dislike');

CREATE TABLE post_reactions (
    id SERIAL PRIMARY KEY,
    post_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    type reaction_type NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE comment_reactions (
    id SERIAL PRIMARY KEY,
    comment_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    type reaction_type NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (comment_id) REFERENCES comments(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Adding indexes to improve retrieval speed
CREATE INDEX idx_post_reactions_post_id ON post_reactions(post_id);
CREATE INDEX idx_comment_reactions_comment_id ON comment_reactions(comment_id);



