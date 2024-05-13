-- Insert users
INSERT INTO users (username) VALUES
('Aatish'), 
('Shashank'),
('Bhavin'),
('Kavish');

-- Insert posts
INSERT INTO posts (user_id, title, content) VALUES
(1, 'First Post', 'This is the content of the first post'),
(2, 'Second Post', 'Content for the second post'),
(3, 'Kavish''s Insights', 'Detailed insights shared by Bhavin'),
(4, 'Thoughts by Kavish', 'Kavish shares his thoughts here');

-- Insert comments
INSERT INTO comments (post_id, parent_id, user_id, content) VALUES
(1, NULL, 2, 'Great post!'),
(1, NULL, 3, 'Thanks for sharing'),
(2, NULL, 1, 'Interesting thoughts'),
(1, 1, 4, 'I agree with Jane');


-- Insert reactions for comments
INSERT INTO comment_reactions (comment_id, user_id, type) VALUES
(1, 3, 'like'),
(2, 1, 'like'),
(3, 4, 'dislike'),
(4, 2, 'like');

-- Insert reactions for posts
INSERT INTO post_reactions (post_id, user_id, type) VALUES
(1, 4, 'like'),
(1, 2, 'dislike'),
(2, 3, 'like'),
(3, 1, 'dislike');
