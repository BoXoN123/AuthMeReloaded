package fr.xephi.authme.command.executable.authme;

import fr.xephi.authme.command.CommandService;
import fr.xephi.authme.util.BukkitService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link GetIpCommand}.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetIpCommandTest {

    @InjectMocks
    private GetIpCommand command;

    @Mock
    private CommandService commandService;

    @Mock
    private BukkitService bukkitService;

    @Mock
    private CommandSender sender;

    @Test
    public void shouldGetIpOfPlayer() {
        // given
        given(bukkitService.getPlayerExact(anyString())).willReturn(null);

        // when
        command.executeCommand(sender, Collections.singletonList("Testt"), commandService);

        // then
        verify(bukkitService).getPlayerExact("Testt");
        verify(sender).sendMessage(argThat(containsString("not online")));
    }

    @Test
    public void shouldReturnIpAddressOfPlayer() {
        // given
        String playerName = "charlie";
        String ip = "123.34.56.88";
        Player player = mockPlayer(playerName, ip);
        given(bukkitService.getPlayerExact(playerName)).willReturn(player);

        // when
        command.executeCommand(sender, Collections.singletonList(playerName), commandService);

        // then
        verify(bukkitService).getPlayerExact(playerName);
        verify(sender).sendMessage(argThat(allOf(containsString(playerName), containsString(ip))));
    }

    private static Player mockPlayer(String name, String ip) {
        Player player = mock(Player.class);
        given(player.getName()).willReturn(name);
        InetAddress inetAddress = mock(InetAddress.class);
        given(inetAddress.getHostAddress()).willReturn(ip);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, 8093);
        given(player.getAddress()).willReturn(inetSocketAddress);
        return player;
    }
}
