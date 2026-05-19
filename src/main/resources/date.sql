-- カテゴリーテーブルデータ
INSERT INTO genres(genre_name, is_income) VALUES('収入', true);
INSERT INTO genres(genre_name, is_income) VALUES('固定費', false);
INSERT INTO genres(genre_name, is_income) VALUES('変動費', false);
INSERT INTO genres(genre_name, is_income) VALUES('その他の収入', true);
INSERT INTO genres(genre_name, is_income) VALUES('その他の支出', false);

-- ユーザテーブルデータ
INSERT INTO users(user_name,email,password) VALUES("田中太郎", "tanaka@aaa.com","himitu");
INSERT INTO users(user_name,email,password) VALUES("鈴木一郎", "suzuki@aaa.com","himitu");

-- 項目テーブルデータ
INSERT INTO items(item_name, user_id, genre_id, price,add_on) VALUES("食事代", 1, 3, 1200, "2026/05/01");
INSERT INTO items(item_name, user_id, genre_id, price,add_on) VALUES("収入", 2, 1, 200000, "2026/05/15");