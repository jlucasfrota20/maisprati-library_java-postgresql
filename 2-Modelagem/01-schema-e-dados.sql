CREATE TABLE item (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    titulo VARCHAR(200) NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('livro', 'revista')),
    autor VARCHAR(150),
    edicao INTEGER,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('aluno', 'professor')),
    limite_itens INTEGER NOT NULL CHECK (
        (tipo = 'aluno' AND limite_itens = 3)
        OR (tipo = 'professor' AND limite_itens = 5)
    )
);

CREATE TABLE emprestimo (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES item(id),
    usuario_id INTEGER NOT NULL REFERENCES usuario(id),
    data_retirada DATE NOT NULL,
    data_devolucao_prevista DATE NOT NULL,
    data_devolucao DATE,
    valor_multa NUMERIC(10, 2) NOT NULL DEFAULT 0 CHECK (valor_multa >= 0),
    CHECK (data_devolucao_prevista >= data_retirada),
    CHECK (data_devolucao IS NULL OR data_devolucao >= data_retirada)
);

CREATE UNIQUE INDEX emprestimo_item_aberto_unique
    ON emprestimo (item_id)
    WHERE data_devolucao IS NULL;

INSERT INTO item (codigo, titulo, tipo, autor, edicao, disponivel) VALUES
    ('LIV-001', 'Dom Casmurro', 'livro', 'Machado de Assis', NULL, FALSE),
    ('LIV-002', 'O Cortico', 'livro', 'Aluisio Azevedo', NULL, TRUE),
    ('REV-001', 'Ciencia Hoje', 'revista', NULL, 421, TRUE),
    ('REV-002', 'National Geographic', 'revista', NULL, 8, TRUE);

INSERT INTO usuario (nome, tipo, limite_itens) VALUES
    ('Ana Souza', 'aluno', 3),
    ('Carlos Lima', 'professor', 5);

INSERT INTO emprestimo (
    item_id,
    usuario_id,
    data_retirada,
    data_devolucao_prevista,
    data_devolucao,
    valor_multa
)
SELECT item.id, usuario.id, CURRENT_DATE - 2, CURRENT_DATE + 12, NULL, 0
FROM item
CROSS JOIN usuario
WHERE item.codigo = 'LIV-001'
  AND usuario.nome = 'Ana Souza'
  AND usuario.tipo = 'aluno';

INSERT INTO emprestimo (
    item_id,
    usuario_id,
    data_retirada,
    data_devolucao_prevista,
    data_devolucao,
    valor_multa
)
SELECT item.id, usuario.id, CURRENT_DATE - 20, CURRENT_DATE - 6, CURRENT_DATE - 3, 1.50
FROM item
CROSS JOIN usuario
WHERE item.codigo = 'LIV-002'
  AND usuario.nome = 'Carlos Lima'
  AND usuario.tipo = 'professor';
