package br.com.triadworks.issuetracker.service.impl;

import java.util.List;

import javax.inject.Named;

import org.conventionsframework.exception.BusinessException;
import org.conventionsframework.qualifier.PersistentClass;
import org.conventionsframework.service.impl.BaseServiceImpl;
import org.hibernate.criterion.MatchMode;

import br.com.triadworks.issuetracker.model.Usuario;
import br.com.triadworks.issuetracker.service.UsuarioService;

@Named("usuarioService")
@PersistentClass(Usuario.class) 
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario> implements UsuarioService {


	private static final long serialVersionUID = 1L;

	@Override
	public List<Usuario> listaTudo() {
		return crud.listAll();
	}

	@Override
	public void salva(Usuario usuario) {
		if(isUsuarioExistente(usuario)){
			throw new BusinessException("Usuário com o login:"+usuario.getLogin() + " já cadastrado em nossa base de dados.");
		}
		super.store(usuario);
	}


	@Override
	public void atualiza(Usuario usuario) {
		crud.saveOrUpdate(usuario);
		
	}

	@Override
	public Usuario carrega(Long id) {
		return crud.load(id);
	}

	@Override
	public Usuario buscaPor(String login, String senha) {
		return (Usuario) crud.eq("login", login)
				.eq("senha", senha).find();
	}

	@Override
	public boolean isUsuarioExistente(Usuario usuario) {
		//usando para ignorar id do usuario que estamos editando senão o rowCount retorna o proprio usuario
		crud.ne("id", usuario.getId());

		if(usuario != null && !"".endsWith(usuario.getLogin())){
			crud.ilike("login", usuario.getLogin(), MatchMode.EXACT);
			return (crud.count() > 0);
 		}
		return false;
	}
	 
}
