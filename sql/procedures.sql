-- Execute no MariaDB (banco contratacao_estagio) antes de usar os endpoints novos.
USE contratacao_estagio;

DROP PROCEDURE IF EXISTS encerrar_contratos_vencidos;

DELIMITER $$
CREATE PROCEDURE encerrar_contratos_vencidos(OUT total INT)
BEGIN
    UPDATE contrato_estagio
       SET ativo = 0
     WHERE ativo = 1
       AND data_termino < CURDATE();

    SET total = ROW_COUNT();
END$$
DELIMITER ;
