SELECT codigo, titulo, tipo, disponivel
FROM item
ORDER BY codigo;

SELECT usuario.nome AS nome_usuario, item.titulo AS titulo_item
FROM emprestimo
JOIN usuario ON usuario.id = emprestimo.usuario_id
JOIN item ON item.id = emprestimo.item_id
WHERE emprestimo.data_devolucao IS NULL
ORDER BY usuario.nome, item.titulo;

SELECT usuario.id, usuario.nome, COALESCE(SUM(emprestimo.valor_multa), 0)::NUMERIC(10, 2) AS total_multas
FROM usuario
LEFT JOIN emprestimo ON emprestimo.usuario_id = usuario.id
GROUP BY usuario.id, usuario.nome
ORDER BY usuario.nome;

SELECT item.codigo, item.titulo, item.tipo
FROM item
LEFT JOIN emprestimo ON emprestimo.item_id = item.id
WHERE emprestimo.item_id IS NULL
ORDER BY item.codigo;
