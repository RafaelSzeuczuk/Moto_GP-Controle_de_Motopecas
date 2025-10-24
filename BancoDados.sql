-- Tabela de Clientes
CREATE TABLE Clientes (
    clienteId SERIAL PRIMARY KEY,
    nome VARCHAR(80) NOT NULL
);

-- Tabela de Peças
CREATE TABLE Pecas (
    pecaId SERIAL PRIMARY KEY,
    nome VARCHAR(80) NOT NULL,
    marca VARCHAR(80),
    modelo VARCHAR(80),
    categoria VARCHAR(80),
    fornecedor VARCHAR(80),
    preco DECIMAL(10,2)
);

-- Tabela de Vendas
CREATE TABLE Vendas (
    vendaId SERIAL PRIMARY KEY,
    clienteId INTEGER NOT NULL,
    dataVenda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valorTotal DECIMAL(10,2),
    CONSTRAINT fk_cliente
        FOREIGN KEY (clienteId)
        REFERENCES Clientes(clienteId)
        ON DELETE CASCADE
);

-- Tabela de Itens de Venda com ON DELETE CASCADE para pecaId
CREATE TABLE ItensVenda (
    itemVendaId SERIAL PRIMARY KEY,
    vendaId INTEGER NOT NULL,
    pecaId INTEGER NOT NULL,
    quantidade INTEGER NOT NULL,
    precoUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_venda
        FOREIGN KEY (vendaId)
        REFERENCES Vendas(vendaId)
        ON DELETE CASCADE,
    CONSTRAINT fk_peca
        FOREIGN KEY (pecaId)
        REFERENCES Pecas(pecaId)
        ON DELETE CASCADE
);

-- Inserção de Clientes
INSERT INTO Clientes (nome) VALUES
('João Silva'),
('Maria Oliveira'),
('Carlos Souza'),
('Ana Martins'),
('Lucas Pereira'),
('Fernanda Lima'),
('Rafael Torres'),
('Bruna Costa');

-- Inserção de Peças
INSERT INTO Pecas (nome, marca, modelo, categoria, fornecedor, preco) VALUES
('Filtro de óleo', 'Honda', 'CG 160', 'Motor', 'MotoParts Honda', 29.90),
('Pastilha de freio', 'Yamaha', 'Factor 125', 'Freio', 'Yamaha Distribuidora', 45.00),
('Amortecedor traseiro', 'Suzuki', 'Yes 125', 'Suspensão', 'Suzuki Peças', 120.00),
('Cabo de embreagem', 'Honda', 'Titan 150', 'Motor', 'MotoParts Honda', 35.50),
('Kit relação', 'Yamaha', 'YBR 125', 'Transmissão', 'Yamaha Distribuidora', 150.00),
('Lanterna traseira', 'Suzuki', 'Intruder 250', 'Elétrica', 'Suzuki Peças', 85.00),
('Bateria 12V', 'Top Motos', 'Universal', 'Elétrica', 'Top Motos', 210.00),
('Pneu traseiro 100/90', 'Racing Imports', 'CG 160', 'Rodas', 'Racing Imports', 230.00),
('Velas de ignição', 'Bosch', 'Universal', 'Motor', 'Bosch Auto', 25.00),
('Retrovisor esquerdo', 'Honda', 'CG 160', 'Carroceria', 'MotoParts Honda', 40.00),
('Disco de freio', 'Yamaha', 'YBR 125', 'Freio', 'Yamaha Distribuidora', 95.00),
('Farol dianteiro', 'Suzuki', 'Yes 125', 'Elétrica', 'Suzuki Peças', 110.00);

-- Inserção de Vendas
INSERT INTO Vendas (clienteId, dataVenda, valorTotal) VALUES
(1, '2025-10-15 10:30:00', 64.40),
(2, '2025-10-15 14:45:00', 275.00),
(3, '2025-10-16 09:20:00', 230.00),
(4, '2025-10-17 11:10:00', 120.00),
(5, '2025-10-17 15:00:00', 160.00),
(6, '2025-10-18 13:45:00', 135.00);

-- Inserção de Itens de Venda
INSERT INTO ItensVenda (vendaId, pecaId, quantidade, precoUnitario, subtotal) VALUES
-- Venda 1: João comprou filtro de óleo + cabo de embreagem
(1, 1, 1, 29.90, 29.90),
(1, 4, 1, 34.50, 34.50),

-- Venda 2: Maria comprou kit relação + bateria
(2, 5, 1, 150.00, 150.00),
(2, 7, 1, 125.00, 125.00),

-- Venda 3: Carlos comprou pneu traseiro
(3, 8, 1, 230.00, 230.00),

-- Venda 4: Ana comprou amortecedor traseiro
(4, 3, 1, 120.00, 120.00),

-- Venda 5: Lucas comprou disco de freio + velas de ignição
(5, 11, 1, 95.00, 95.00),
(5, 9, 2, 32.50, 65.00),

-- Venda 6: Fernanda comprou farol dianteiro + retrovisor
(6, 12, 1, 110.00, 110.00),
(6, 10, 1, 25.00, 25.00);
