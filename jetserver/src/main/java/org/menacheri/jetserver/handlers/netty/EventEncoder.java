package org.menacheri.jetserver.handlers.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.MessageBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import org.menacheri.jetserver.event.Event;


/**
 * A simple event encoder will receive an incoming event, and convert it to a
 * {@link ByteBuf}. It will read the event type and put it as the
 * opcode(i.e first byte of the buffer), then it will read the event body and
 * put convert to ChannelBuffer if necessary and put it as the body of the
 * message.
 * 
 * @author Abraham Menacherry
 * 
 */
@Sharable
public class EventEncoder extends MessageToMessageEncoder<Event>
{
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Event event,
			MessageBuf<Object> out) throws Exception
	{
		ByteBuf opcode = ctx.alloc().buffer(1);
		opcode.writeByte(event.getType());
		if(null != event.getSource())
		{
			ByteBuf data = (ByteBuf)event.getSource();
			CompositeByteBuf compositeBuffer = ctx.alloc().compositeBuffer(2);
			compositeBuffer.addComponents(opcode,data);
			out.add(compositeBuffer);
		}
		else 
		{
			out.add(opcode);
		}
		
	}
	
}
